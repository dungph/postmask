use pgx::prelude::*;
use rand::seq::IteratorRandom;

use crate::current_user_can_unmask_object;

const FIRST_NAMES: &str = include_str!("./faking_data/first_names.txt");
const MID_NAMES: &str = include_str!("./faking_data/middle_names.txt");
const LAST_NAMES: &str = include_str!("./faking_data/last_names.txt");
const PROVINDES: &str = include_str!("./faking_data/provides.txt");

#[pg_extern]
#[no_mangle]
fn mask_str_with_fake_name(object: &str, value: &str) -> Result<String, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value.to_owned())
    } else {
        Ok(random_name())
    }
}

#[pg_extern]
#[no_mangle]
fn mask_str_with_fake_provinde(object: &str, value: &str) -> Result<String, pgx::spi::Error> {
    if current_user_can_unmask_object(object)? {
        Ok(value.to_owned())
    } else {
        Ok(random_provide())
    }
}

#[pg_extern]
#[no_mangle]
fn random_name() -> String {
    let mut rng = rand::thread_rng();
    let first = FIRST_NAMES.lines().choose(&mut rng).unwrap();
    let mid = MID_NAMES.lines().choose(&mut rng).unwrap();
    let last = LAST_NAMES.lines().choose(&mut rng).unwrap();
    [first, mid, last].join(" ").replace("  ", " ")
}

#[pg_extern]
#[no_mangle]
fn random_provide() -> String {
    let mut rng = rand::thread_rng();
    PROVINDES.lines().choose(&mut rng).unwrap().to_string()
}
