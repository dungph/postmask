use pgx::prelude::*;

use crate::current_user_can_unmask_object;
#[pg_extern]
#[no_mangle]
fn mask_str(object: &str, value: &str) -> Result<String, pgx::spi::Error> {
    mask_str_replace(object, value, "MASKED")
}

#[pg_extern]
#[no_mangle]
fn mask_str_replace(
    object: &str,
    value: &str,
    replaced_with: &str,
) -> Result<String, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(String::from(value))
    } else {
        Ok(String::from(replaced_with))
    }
}
