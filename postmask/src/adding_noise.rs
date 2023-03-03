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
        let mut epoch = value.to_pg_epoch_days();

        epoch = epoch + (epoch as f32 * ratio * (rand::random::<f32>() - 0.5) * 2.0) as i32;
        Ok(Date::from_pg_epoch_days(epoch))
    }
}
