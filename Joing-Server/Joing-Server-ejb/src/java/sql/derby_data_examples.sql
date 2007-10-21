-- ********************************************************************************************************************
--     WebPC - SERVER - DATA
-- ********************************************************************************************************************

insert into locales (IDIOM, COUNTRY)    -- ID_LOCALE = 2
     values ('es', 'ES');

-- ------------------------------------------------------------------
-- There is no problem about the same person being twice: peyrona has to profiles, one
-- as admin and annother as simple user.

insert into users (ACCOUNT, PASSWORD, EMAIL, FIRST_NAME, SECOND_NAME, ID_LOCALE, IS_MALE, QUOTA)
     values('peyrona@joing.peyrona.com','admin','peyrona@gmail.com', 'Francisco','Morero Peyrona', 2, 1, 0);

-- ------------------------------------------------------------------

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 1
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 2
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 3
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 4
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 5
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 6
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 7
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 8
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 9
     values ( NULL, NULL );

-- ------------------------------------------------------------------

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (1, 1, 'Business');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (1, 2, 'Negocios');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (2, 1, 'Desktop');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (2, 2, 'Escritorio');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (3, 1, 'Games');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (3, 2, 'Juegos');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (4, 1, 'Home & Education');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (4, 2, 'Hogar y Educativo');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (5, 1, 'Internet');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (5, 2, 'Internet');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (6, 1, 'Multimedia');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (6, 2, 'Multimedia');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (7, 1, 'Security');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (7, 2, 'Seguridad');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (8, 1, 'Programming');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (8, 2, 'Programación');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (9, 1, 'System Utilities');

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (9, 2, 'Utilidades del sistema');

-- ------------------------------------------------------------------

insert into applications (APPLICATION, VERSION, APP_DOMAIN, EXTRA_PATH, EXECUTABLE, FILE_TYPES)
     values ('Notes', '0.1', 'accessories', 'notes.jar', 'txt;ini');

insert into applications (APPLICATION, VERSION, EXTRA_PATH, EXECUTABLE)
     values ('Calculator', '2.4', 'accessories', 'calculator.jar');

insert into applications (APPLICATION, VERSION, EXTRA_PATH, EXECUTABLE)
     values ('Images', '1.1', 'graphics', 'images.jar');

insert into applications (APPLICATION, VERSION, EXTRA_PATH, EXECUTABLE)
     values ('PhotoArt', '0.5', 'graphics', 'photoart.jar');

-- ------------------------------------------------------------------

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (1, 1, 'A very simple text editor' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (1, 2, 'Un editor de textos muy simple' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (2, 1, 'Calculator - Basic and Scientific' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (2, 2, 'Calculadora - Básica y Científica' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (3, 1, 'Simple image viewer with support for several graphic formats' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (3, 2, 'Visualizador de imágenes básico con soporte para varios formatos' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (4, 1, 'The best photo retouchery application ever' );

insert into app_descriptions (ID_APPLICATION, ID_LOCALE, DESCRIPTION)
     values (4, 2, 'El mejor programa para retoque fotográfico' );

-- ------------------------------------------------------------------

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (1, 1);   -- App 'Notes' belongs to Group 'Accessories'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (2, 1);   -- App 'Calculator' belongs to Group 'Accessories'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (3, 2);   -- App 'Images' belongs to Group 'Graphics'

insert into apps_with_groups (ID_APPLICATION, ID_APP_GROUP)
     values (4, 2);   -- App 'Images' belongs to Group 'Graphics'

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
     values ('peyrona@joing.peyrona.com', 3, 1, 1);

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