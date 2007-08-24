-- ********************************************************************************************************************
--     WebPC - SERVER - DATA
-- ********************************************************************************************************************

insert into locales (IDIOM, COUNTRY)    -- ID_LOCALE = 1
     values ('en', 'US');

-- ------------------------------------------------------------------
-- Join'g at 'joing.peyrona' community administrator account

insert into users (ACCOUNT, PASSWORD, EMAIL, FIRST_NAME, SECOND_NAME, ID_LOCALE, IS_MALE, QUOTA)
     values('admin@joing.peyrona.com','adminadmin','francisco@peyrona.com', 'Francisco','Morero Peyrona', 1, 1, 0);

-- *****************************************   EOF  *******************************************************************