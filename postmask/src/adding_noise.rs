use pgx::{prelude::*, spi::Spi};

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
