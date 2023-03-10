use pgx::prelude::*;
use time::Month;

use crate::current_user_can_unmask_object;

#[pg_extern]
#[no_mangle]
fn mask_str_with_partial(
    object: &str,
    value: &str,
    begin: i32,
    replace: &str,
    end: i32,
) -> Result<String, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value.to_owned())
    } else {
        let mut string = String::new();
        string.push_str(&value[..begin as usize]);
        string.push_str(replace);
        string.push_str(&value[(value.len() - end as usize)..]);
        Ok(string)
    }
}

#[pg_extern]
#[no_mangle]
fn mask_str_with_partial_email(object: &str, value: &str) -> Result<String, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value.to_owned())
    } else {
        let b = &value.chars().next().unwrap();
        let e = &value.chars().last().unwrap();
        Ok(format!("{b}xxxxx@gxxx.xx{e}"))
    }
}

#[pg_extern]
#[no_mangle]
fn mask_date_with_only_year(object: &str, value: Date) -> Result<Date, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value)
    } else {
        Ok(time::Date::try_from(value)
            .unwrap()
            .replace_month(Month::January)
            .unwrap()
            .replace_day(1)
            .unwrap()
            .into())
    }
}
