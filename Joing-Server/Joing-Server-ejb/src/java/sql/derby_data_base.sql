-- ********************************************************************************************************************
--     WebPC - SERVER - DATA
-- ********************************************************************************************************************

insert into locales (IDIOM, COUNTRY)    -- ID_LOCALE = 1
     values ('en', 'US');

-- ------------------------------------------------------------------

-- For inf about ID_APP_GROUP, refer to -> org.joing.common.dto.app.AppGroup.java

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (1, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (2, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (3, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (4, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (5, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (6, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (7, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (8, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (9, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (10, NULL, NULL);

insert into app_groups (ID_APP_GROUP, ICON_PNG, ICON_SVG)
     values (99, NULL, NULL);

-- ------------------------------------------------------------------

-- For inf about ID_APP_GROUP, refer to -> org.joing.common.dto.app.AppGroup.java

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (1, 1, 'Accesories');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (2, 1, 'Education');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (3, 1, 'Games');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (4, 1, 'Graphics');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (5, 1, 'Internet');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (6, 1, 'Multimedia');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (7, 1, 'Office');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (8, 1, 'Programming');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (9, 1, 'System');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (10, 1, 'Other');

insert into app_group_descriptions (ID_APP_GROUP, ID_LOCALE, GROUP_NAME)
     values (99, 1, 'Desktops');

-- *****************************************   EOF  *******************************************************************