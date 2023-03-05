use std::time::Duration;

use pgx::prelude::*;

use crate::current_user_can_unmask_object;

#[pg_extern]
#[no_mangle]
fn mask_integer_with_noise(object: &str, value: i32, ratio: f32) -> Result<i32, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value)
    } else {
        Ok(value + (value as f32 * ratio * (rand::random::<f32>() - 0.5) * 2.0) as i32)
    }
}

#[pg_extern]
#[no_mangle]
fn mask_date_with_noise(object: &str, value: Date, ratio: f32) -> Result<Date, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value)
    } else {
        let date: time::Date = value.try_into().unwrap();

        let year = date.year();
        let diff_secs = rand::random::<u64>() % ((year as f32 * ratio) as u64 * 365 * 24 * 60 * 60);
        let date = if rand::random() {
            date + Duration::from_secs(diff_secs)
        } else {
            date - Duration::from_secs(diff_secs)
        };
        Ok(date.into())
    }
}
