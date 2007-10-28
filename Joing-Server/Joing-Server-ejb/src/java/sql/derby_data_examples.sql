-- ********************************************************************************************************************
--     WebPC - SERVER - DATA
-- ********************************************************************************************************************

insert into locales (IDIOM, COUNTRY)    -- ID_LOCALE = 2
     values ('es', 'ES');

-- ------------------------------------------------------------------
-- There is no problem about the same person being twice: peyrona has to profiles, 
-- one as admin and annother as simple user.

insert into users (ACCOUNT, PASSWORD, EMAIL, FIRST_NAME, SECOND_NAME, ID_LOCALE, IS_MALE, QUOTA)
     values('peyrona@joing.peyrona.com','admin','peyrona@gmail.com', 'Francisco','Morero Peyrona', 2, 1, 0);

-- ------------------------------------------------------------------

-- For inf about ID_APP_GROUP, refer to -> org.joing.common.dto.app.AppGroup.java

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (1, 2, 'Accesorios');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (2, 2, 'Educativos');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (3, 2, 'Juegos');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (4, 2, 'Gráficos');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (5, 2, 'Internet');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (6, 2, 'Multimedia');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (7, 2, 'Oficina');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (8, 2, 'Programación');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (9, 2, 'Sistema');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (10, 2, 'Otros');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (99, 2, 'Escritorios');

-- ------------------------------------------------------------------

-- For inf about ENVIRONMENT, refer to -> org.joing.common.dto.app.AppEnvironment.java
insert into applications (APPLICATION, VERSION, EXTRA_PATH, EXECUTABLE, FILE_TYPES, ENVIRONMENT, ENVIRON_VER)
     values ('PDE', '0.0.1', 'desktops', 'PDE.jar', null, 3, '1.5');

insert into applications (APPLICATION, VERSION, EXTRA_PATH, EXECUTABLE, FILE_TYPES, ENVIRONMENT, ENVIRON_VER)
     values ('Notes', '0.1', 'accessories', 'Notes.jar', 'txt;ini', 3, '1.5');

insert into applications (APPLICATION, VERSION, EXTRA_PATH, EXECUTABLE, ENVIRONMENT, ENVIRON_VER)
     values ('Calculator', '2.4', 'accessories', 'Calculator.jar', 3, '1.5');

insert into applications (APPLICATION, VERSION, EXTRA_PATH, EXECUTABLE, ENVIRONMENT, ENVIRON_VER)
     values ('Images', '1.1', 'graphics', 'Images.jar', 3, '1.5');

insert into applications (APPLICATION, VERSION, EXTRA_PATH, EXECUTABLE, ENVIRONMENT, ENVIRON_VER)
     values ('Tetris', '0.5', 'games', 'Tetris.jar', 3, '1.2');

-- ------------------------------------------------------------------

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (1, 1, 'Peyrona Desktop Environment' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (1, 2, 'Peyrona Desktop Environment' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (2, 1, 'A very simple text editor' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (2, 2, 'Un editor de textos muy simple' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (3, 1, 'Calculator - Basic and Scientific' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (3, 2, 'Calculadora - Básica y Científica' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (4, 1, 'Simple image viewer with support for several graphic formats' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (4, 2, 'Visualizador de imágenes básico con soporte para varios formatos' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (5, 1, 'Tetris: as fun as simple' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (5, 2, 'Tetris: tan divertido como simple' );

-- ------------------------------------------------------------------

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (1, 99);   -- App 'PDE' belongs to Group 'Desktops'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (2, 1);   -- App 'Notes' belongs to Group 'Accessories'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (3, 1);   -- App 'Calculator' belongs to Group 'Accessories'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (4, 4);   -- App 'Images' belongs to Group 'Graphics'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (5, 3);   -- App 'Images' belongs to Group 'Games'

-- ------------------------------------------------------------------

insert into APP_PREFERRED (ID_APPLICATION, FILE_EXTENSION)
     values (1, 'txt');

insert into APP_PREFERRED (ID_APPLICATION, FILE_EXTENSION)
     values (1, 'ini');

-- ------------------------------------------------------------------

insert into users_with_apps (ACCOUNT, ID_APPLICATION, IS_INSTALLED, ALLOW_REMOTE)
     values ('peyrona@joing.peyrona.com', 1, 1, 1);
 
insert into users_with_apps (ACCOUNT, ID_APPLICATION, IS_INSTALLED, ALLOW_REMOTE)
     values ('peyrona@joing.peyrona.com', 2, 1, 1);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, IS_INSTALLED, ALLOW_REMOTE)
     values ('peyrona@joing.peyrona.com', 3, 1, 0);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, IS_INSTALLED, ALLOW_REMOTE)
     values ('peyrona@joing.peyrona.com', 4, 1, 0);

-- ------------------------------------------------------------------

insert into files ( ID_ORIGINAL, ACCOUNT, OWNER, FILE_NAME, FILE_PATH, LOCKED_BY, 
                    IS_DIR, IS_HIDDEN, IS_PUBLIC, IS_READABLE, IS_MODIFIABLE, IS_DELETEABLE, 
                    IS_EXECUTABLE, IS_DUPLICABLE, IS_ALTERABLE, IS_IN_TRASHCAN, NOTES )
           values ( NULL, 'peyrona@joing.peyrona.com', 'system@joing.peyrona.com', '/', '', NULL, 
                    1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 
                   'Root & Home Directory');

insert into files ( ID_ORIGINAL, ACCOUNT, OWNER, FILE_NAME, FILE_PATH, LOCKED_BY, 
                    IS_DIR, IS_HIDDEN, IS_PUBLIC, IS_READABLE, IS_MODIFIABLE, IS_DELETEABLE, 
                    IS_EXECUTABLE, IS_DUPLICABLE, IS_ALTERABLE, IS_IN_TRASHCAN, NOTES )
           values ( NULL, 'peyrona@joing.peyrona.com', 'system@joing.peyrona.com', 'Desktop', '/', NULL,
                    1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 
                   'The desktop');

insert into files ( ID_ORIGINAL, ACCOUNT, OWNER, FILE_NAME, FILE_PATH, LOCKED_BY, 
                    IS_DIR, IS_HIDDEN, IS_PUBLIC, IS_READABLE, IS_MODIFIABLE, IS_DELETEABLE, 
                    IS_EXECUTABLE, IS_DUPLICABLE, IS_ALTERABLE, IS_IN_TRASHCAN, NOTES )
           values ( NULL, 'peyrona@joing.peyrona.com', 'peyrona@joing.peyrona.com', 'The very 1st file', '/', NULL, 
                    0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 
                   'The very first file (not dir). Originaly stored in root dir');

-- *****************************************   EOF  *******************************************************************