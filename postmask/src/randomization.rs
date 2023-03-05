use std::time::Duration;

use pgx::prelude::*;
use rand::random;

use crate::current_user_can_unmask_object;

#[pg_extern]
#[no_mangle]
fn mask_date_with_random_date(object: &str, value: Date) -> Result<pgx::Date, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value)
    } else {
        let range = (time::Date::MAX - time::Date::MIN).whole_seconds().abs() as u64;
        let date = time::Date::MIN + Duration::from_secs(rand::random::<u64>() % range);

        Ok(date.into())
    }
}

#[pg_extern]
#[no_mangle]
fn mask_date_with_random_date_between(
    object: &str,
    value: Date,
    min: Date,
    max: Date,
) -> Result<pgx::Date, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value)
    } else {
        let range = (time::Date::try_from(max).unwrap() - time::Date::try_from(min).unwrap())
            .whole_seconds()
            .abs() as u64;
        let date = time::Date::MIN + Duration::from_secs(rand::random::<u64>() % range);

        Ok(date.into())
    }
}

#[pg_extern]
#[no_mangle]
fn mask_str_with_random_str(object: &str, value: &str, n: i32) -> Result<String, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value.to_owned())
    } else {
        let mut s = String::new();
        for _ in 0..n {
            let c = random::<char>();
            s.push(c);
        }
        Ok(s)
    }
}

#[pg_extern]
#[no_mangle]
fn mask_int_with_random_int(object: &str, value: i32) -> Result<i32, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value.to_owned())
    } else {
        Ok(random::<i32>())
    }
}

#[pg_extern]
#[no_mangle]
fn mask_int_with_random_int_between(
    object: &str,
    value: i32,
    min: i32,
    max: i32,
) -> Result<i32, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value.to_owned())
    } else {
        let range = max - min + 1;

        let num = random::<i32>() % range;

        Ok(num + min)
    }
}

#[pg_extern]
#[no_mangle]
fn mask_str_with_random_phone(
    object: &str,
    value: &str,
    prefix: &str,
    n: i32,
) -> Result<String, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value.to_owned())
    } else {
        let mut s = String::from(prefix);
        for _ in 0..n {
            let n = random::<i32>() % 10;
            s.push_str(n.to_string().as_str());
        }
        Ok(s)
    }
}
