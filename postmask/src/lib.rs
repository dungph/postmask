mod adding_noise;
mod destruction;
mod faking;
mod randomization;
use pgx::{prelude::*, spi::Spi};
pgx::pg_module_magic!();

extension_sql!(
    r#"
create table unmask_user (
    unmask_user text not null,
    unmask_group text not null
);
create table unmask_object (
    unmask_object text not null,
    unmask_group text not null
);
"#,
    name = "create_unmask_table",
);

#[pg_extern]
#[no_mangle]
fn assign_user_to_group(user: &str, group: &str) -> Result<(), pgx::spi::Error> {
    Spi::get_one::<String>(
        format!(
            r#"
insert into unmask_user
values ('{}', '{}')
returning unmask_user
"#,
            user, group
        )
        .as_str(),
    )?;
    Ok(())
}
#[pg_extern]
#[no_mangle]
fn remove_user_from_group(user: &str, group: &str) -> Result<(), pgx::spi::Error> {
    Spi::get_one::<String>(
        format!(
            r#"
delete from unmask_user 
where unmask_user = '{}' 
and unmask_group = '{}' 
returning unmask_user
            "#,
            user, group
        )
        .as_str(),
    )?;
    Ok(())
}

#[pg_extern]
#[no_mangle]
fn remove_user_from_all_group(user: &str) -> Result<(), pgx::spi::Error> {
    Spi::get_one::<String>(
        format!(
            r#"
delete from unmask_user 
where unmask_user = '{}' 
returning unmask_user
            "#,
            user
        )
        .as_str(),
    )?;
    Ok(())
}

#[pg_extern]
#[no_mangle]
fn assign_object_to_group(object: &str, group: &str) -> Result<(), pgx::spi::Error> {
    Spi::get_one::<String>(
        format!(
            r#"
insert into unmask_object (unmask_object, unmask_group)
values ('{}', '{}') 
returning unmask_object
            "#,
            object, group
        )
        .as_str(),
    )?;
    Ok(())
}

#[pg_extern]
#[no_mangle]
fn remove_object_from_group(group: &str, object: &str) -> Result<(), pgx::spi::Error> {
    Spi::get_one::<String>(
        format!(
            r#"
delete from unmask_object
where unmask_object = '{}'
and unmask_group = '{}'
returning unmask_object
            "#,
            object, group
        )
        .as_str(),
    )?;
    Ok(())
}

#[pg_extern]
#[no_mangle]
fn partial_mask_str(object: &str, value: &str) -> Result<String, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(String::from(value))
    } else {
        Ok(if value.len() < 3 {
            String::from("xxxxxx")
        } else {
            format!("{}xxxxxx", &value[..2])
        })
    }
}

fn current_user_can_unmask_object(object: &str) -> Result<bool, pgx::spi::Error> {
    let query = format!(
        r#"
select 0 < (
    select count(*) from unmask_user
    join unmask_object
    on unmask_user.unmask_group = unmask_object.unmask_group
    where unmask_object = '{object}'
    and unmask_user = CURRENT_USER
)
        "#
    );
    Ok(Spi::get_one(&query)?.unwrap_or(false))
}

#[pg_extern]
fn hello_postmask() -> &'static str {
    "Hello, postmask"
}

#[cfg(any(test, feature = "pg_test"))]
#[pg_schema]
mod tests {
    use pgx::prelude::*;

    #[pg_test]
    fn test_hello_postmask() {
        assert_eq!("Hello, postmask", crate::hello_postmask());
    }
}

/// This module is required by `cargo pgx test` invocations.
/// It must be visible at the root of your extension crate.
#[cfg(test)]
pub mod pg_test {
    pub fn setup(_options: Vec<&str>) {
        // perform one-off initialization when the pg_test framework starts
    }

    pub fn postgresql_conf_options() -> Vec<&'static str> {
        // return any postgresql.conf settings that are required for your tests
        vec![]
    }
}
