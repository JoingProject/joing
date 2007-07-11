-- ********************************************************************************************************************
--     WebPC - SERVER - DATA
-- ********************************************************************************************************************

insert into locales (LANGUAGE, COUNTRY)    -- ID_LOCALE = 1
     values ('en', 'US');

insert into locales (LANGUAGE, COUNTRY)    -- ID_LOCALE = 2
     values ('es', 'ES');

-- ------------------------------------------------------------------

insert into users (ACCOUNT, PASSWORD, EMAIL, FIRST_NAME, SECOND_NAME, ID_LOCALE, IS_MALE, QUOTA)
     values('peyrona','admin','peyrona@gmail.com', 'Francisco','Morero Peyrona', 2, 1, 0);

-- ------------------------------------------------------------------

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 1
     values ( NULL, NULL );

insert into app_groups ( ICON_PNG, ICON_SVG )   -- ID_APP_GROUP = 2
     values ( NULL, NULL );

-- ------------------------------------------------------------------

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (1, 1, 'Accessories' );

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (1, 2, 'Accesorios' );

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (2, 1, 'Graphics' );

insert into app_group_descriptions ( ID_APP_GROUP, ID_LOCALE, DESCRIPTION )
     values (2, 2, 'Gráficos' );

-- ------------------------------------------------------------------

insert into applications (NAME, VERSION, EXTRA_PATH, EXECUTABLE, FILE_TYPES)
     values ('Notes', '0.1', 'apps/accessories', 'notes.jar', 'txt;ini');

insert into applications (NAME, VERSION, EXTRA_PATH, EXECUTABLE)
     values ('Calculator', '2.4', 'apps/accessories', 'calculator.jar');

insert into applications (NAME, VERSION, EXTRA_PATH, EXECUTABLE)
     values ('Images', '1.1', 'apps/graphics', 'images.jar');

insert into applications (NAME, VERSION, EXTRA_PATH, EXECUTABLE)
     values ('PhotoArt', '0.5', 'apps/graphics', 'photoart.jar');

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
     values ('peyrona', 1, 1, 1);
 
insert into users_with_apps (ACCOUNT, ID_APPLICATION, IS_INSTALLED, ALLOW_REMOTE)
     values ('peyrona', 2, 1, 1);

insert into users_with_apps (ACCOUNT, ID_APPLICATION, IS_INSTALLED, ALLOW_REMOTE)
     values ('peyrona', 3, 1, 1);

-- *****************************************   EOF  *******************************************************************