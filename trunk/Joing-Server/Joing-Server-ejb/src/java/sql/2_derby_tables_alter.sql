-- ********************************************************************************************************************
--  WebPC - TABLES - EXTERNAL KEY DEFINITIONS 
-- ********************************************************************************************************************

--****************************************************************************
-- USERS
-- Many-to-many: Users with Applications --> Users part
ALTER TABLE USERS_WITH_APPS
     ADD CONSTRAINT FK_UA_USERS
         FOREIGN KEY (ACCOUNT)
         REFERENCES USERS (ACCOUNT)
         ON DELETE CASCADE;
-- Many-to-many: Users with Applications --> Applications part
ALTER TABLE USERS_WITH_APPS
     ADD CONSTRAINT FK_UA_APPLICATIONS
         FOREIGN KEY (ID_APPLICATION)
         REFERENCES APPLICATIONS (ID_APPLICATION)
         ON DELETE CASCADE;

ALTER TABLE USERS
     ADD CONSTRAINT FK_USERS_LOCALES
         FOREIGN KEY (ID_LOCALE)
         REFERENCES LOCALES (ID_LOCALE)
         ON DELETE CASCADE;

-- Many-to-many: Users with Gropus --> Users part
--ALTER TABLE USERS_WITH_GROUPS
--     ADD CONSTRAINT FK_UG_USERS
--         FOREIGN KEY (ACCOUNT)
--         REFERENCES USERS (ACCOUNT)
--         ON DELETE CASCADE;
-- Many-to-many: Users with Gropus --> Groups part
--ALTER TABLE USERS_WITH_GROUPS
--     ADD CONSTRAINT FK_UG_USERGROUPS
--         FOREIGN KEY (ID_USER_GROUP)
--         REFERENCES USER_GROUPS (ID_USER_GROUP)
--        ON DELETE CASCADE;

--****************************************************************************
-- APPLICATIONS

ALTER TABLE APP_GROUP_DESCRIPTIONS
     ADD CONSTRAINT FK_APPGRPNAME_APPGROUPS
         FOREIGN KEY (ID_APP_GROUP)
         REFERENCES APP_GROUPS (ID_APP_GROUP)
         ON DELETE CASCADE;

ALTER TABLE APP_GROUP_DESCRIPTIONS
     ADD CONSTRAINT FK_APPGROUPDESCRIPTIONS_LOCALES
         FOREIGN KEY (ID_LOCALE)
         REFERENCES LOCALES (ID_LOCALE)
         ON DELETE CASCADE;

-- Many-to-many: Aplications with AppGroups --> Applications part
ALTER TABLE APPS_WITH_GROUPS
     ADD CONSTRAINT FK_AG_APPLICATIONS
         FOREIGN KEY (ID_APPLICATION)
         REFERENCES APPLICATIONS (ID_APPLICATION)
         ON DELETE CASCADE;
-- Many-to-many: Aplications with AppGroups --> AppGroups part
ALTER TABLE APPS_WITH_GROUPS
     ADD CONSTRAINT FK_AG_APPGROUPS
         FOREIGN KEY (ID_APP_GROUP)
         REFERENCES APP_GROUPS (ID_APP_GROUP)
         ON DELETE CASCADE;

--****************************************************************************
-- FILES

ALTER TABLE FILES
     ADD CONSTRAINT FK_FILES_USERS
         FOREIGN KEY (ACCOUNT)
         REFERENCES USERS (ACCOUNT)
         ON DELETE CASCADE;

--****************************************************************************
-- SESSIONS

ALTER TABLE SESSIONS
     ADD CONSTRAINT FK_SESSION_USERS
         FOREIGN KEY (ACCOUNT)
         REFERENCES USERS (ACCOUNT)
         ON DELETE CASCADE;

-- *****************************************   EOF    ******************************************************************