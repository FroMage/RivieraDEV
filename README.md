# How to run the Riviera DEV web site

Note: at the moment it requires Play 1.5.3.

1. Download and install [jdk-11](https://jdk.java.net/java-se-ri/11)
    - On Windows: [Installation instructions](https://stackoverflow.com/questions/52511778/how-to-install-openjdk-11-on-windows)
2. Download [Python 2](https://www.python.org/downloads/release/python-2717/) and install it
3. Download [Play Framework 1.5.3](https://downloads.typesafe.com/play/1.5.3/play-1.5.3.zip) and install it
    - [Installation guide](https://www.playframework.com/documentation/1.5.x/install)
4. Clone the [RivieraDEV repository](https://github.com/FroMage/RivieraDEV)
5. Download and install [Postgres 9 or 11](https://www.postgresql.org/download/) on port 5433
    - On Windows, add `<path_to_postres>\bin` to the `Path` in Environment Variables
6. Open a shell and go to the `RivieraDEV` directory you just cloned
7. Create your Postgres DB (9.x)
    - On Linux:
        1. `sudo su - postgres`
        2. `createuser -p 5433 -PSRD rivieradev`
        3. _enter `rivieradev` as password when prompted_
        4. `createdb -p 5433 -O rivieradev -E utf8 rivieradev-2023`
        5. exit
    - On Windows:
        1. `createuser -U postgres -p 5433 -PSRD rivieradev`
        2. _enter `rivieradev` as password when prompted_
        3. _enter the postgres password chosen during the installation when prompted_
        4. `createdb -U postgres -p 5433 -O rivieradev -E utf8 rivieradev-2023`
        5. _enter the postgres password chosen during the installation when prompted_
8. Play (on Windows, you might need to run the following commands in a Powershell run as an Administrator)
    1. Download the module dependencies
        - `play dependencies --forProd`
    2. Run the application
        - `play run`
9. You can visit the website at http://localhost:9001/ (if the text is not translated, just refresh)

# How to make your user (if registration is disabled, which is the default for now)

This can only be done by hand for now:

1. Pick a password
1. Open a `psql` console to your database:
    1. `psql -p 5433 -h localhost -U rivieradev rivieradev-2023`
1. Add your user (as admin)
    1. `INSERT INTO user_table (id, firstname, lastname, password, username) VALUES ((select nextval('hibernate_sequence')), 'FirstName', 'LastName', 'Password', 'UserName');`
1. You can now log in and change your password using the UI

# Configuration

In the admin part (https://\<url\>/admin) there is a special table `Configurations`. All the keys are in defined in `app/models/ConfigurationKey.java`.

Here is how to configure it.

## Launch of the new edition

When we set up the website for the new edition.

-   `GOOGLE_MAP_API_KEY`: Reuse the same key as for the previous edition
-   `EVENT_START_DATE`: Start date of the conference in ISO format. E.g. `2019-05-15T08:20:00`
-   `EVENT_START_DATE`: End date of the conference in ISO format: E.g. `2019-05-17T18:00:00`
-   `DISPLAY_FULL_SCHEDULE = false`: We don't want to display the schedule because we don't know it yet.
-   `DISPLAY_NEW_SPEAKERS = false`: Same as above.
-   `DISPLAY_TALKS = false`: Same as above.
-   `PROMOTED_PAGE`: It's for the primary button shown on the home page below the logo. The possible values are be `CFP`, `TICKETS` or `SPONSORS`. At this time it's a bit tricky because none of them is available 😅 So let's not use it for now.
-   `PROMOTED_PAGE_2`; It's the same as above but for the secondary button. The possible values are `CFP`, `SPONSORS` and `SCHEDULE`. And it's the same, let's not use it for now.
-   `TICKETING_URL`: At this time the ticketing is not opened yet, but if we already know the URL, we can fill it.
-   `TICKETING_OPEN = false`
-   `TICKETING_TRAINING_URL`: To be filled with the URL provided by the training organization, but you probably don't know it yet
-   `TICKETING_TRAINING_OPEN = false` The training organization is not ready yet
-   `SPONSORING_LEAFLET_URL`: URL to the sponsoring leaflet.
-   `CFP_URL`: At this time the CFP is not opened yet, but if we know the URL, we can fill it.
-   `CFP_OPEN = false`

## When the sponsoring leaflet is ready

-   `SPONSORING_LEAFLET_URL`: URL to the sponsoring leaflet if not already filled.
-   `PROMOTED_PAGE = 'SPONSORS'`

## When we open the CFP

-   `CFP_URL`: Fill it if it's not already done.
-   `CFP_OPEN = true`
-   `PROMOTED_PAGE = CFP`
-   `PROMOTED_PAGE_2 = SPONSORS` as soon as the leaflet is ready

## When we open the ticketing

-   `TICKETING_URL`: Fill it if it's not already done.
-   `TICKETING_OPEN = true`

Don't forget to fill the tables `PricePacks` and `PricePackDates`.

## When the training organization is ready

-   `TICKETING_TRAINING_URL`: Fill it if it's not already done.
-   `TICKETING_TRAINING_OPEN = true`

## When we close the CFP

-   `CFP_OPEN = false`
-   `PROMOTED_PAGE = TICKETS`

## When some talks and speakers are known

Before changing the configuration, we need to add some talks and speakers.

-   `DISPLAY_TALKS = true`
-   `DISPLAY_NEW_SPEAKERS = true`

## When the full schedule is known

-   `DISPLAY_FULL_SCHEDULE = true`
-   `PROMOTED_PAGE_2 = SCHEDULE`

## When we close the ticketing

-   `TICKETING_OPEN = true` Yes, you read correctly, we don't change the value.
-   Check the checkbox `soldout` in each concerned `PricePacks`
-   `PROMOTED_PAGE = SPONSORS`

# Publishing a new website

- On your local machine
- Make changes
- Test it locally
	- `play run`
- Commit the files to git, push
- Build a debian package
	- `fakeroot ./debian/rules clean binary`
- Copy the package to the production machine
	- `scp ../rivieradev-2023_1.17_all.deb ssh.inforealm.org:`
- Log into the production machine for the following commands
	- `ssh ssh.inforealm.org`
- Install the new version and restart it
	- `sudo dpkg -i rivieradev-2023_1.17_all.deb`

## Additional steps if there are DB changes

- Locally, extract the new schema (use a number increment by looking at the files in `db/`
	- `play db:export > db/db-X.sql`
	- Edit the file to remove the Play header
- Create an upgrade script manually, by starting with the diff which is a good hint generally:
	- `diff -u db/db-(X-1).sql db/db-X.sql > db/upgrade-X.sql`
	- Edit the file to turn it into a SQL upgrade script modifying tables
	- Don't forget to update the rows too
	- If you add a new column with a constraint, do it in three steps:
		- Add the column without the constraint
		- Update the rows to give a proper value to the new column
		- Alter the column to add the constraint
- Add the files to git, commit, push
- Copy the upgrade file to the production machine:
	- `scp db/upgrade-X.sql ssh.inforealm.org:`
- Log into the production machine for the following commands
	- `ssh ssh.inforealm.org`
- Run the upgrade script
	- `psql -h localhost -U rivieradev rivieradev-2023`
	- `\i upgrade-X.sql`

## Making a new RivieraDEV edition

- Locally
- In your IDE, do a global replace of 2023 to 2024 (except in `debian/changelog` and a few others)
- Rename the files in `debian/` whose name contain 2023 to 2024
- Add a new entry with the new package name in `debian/changelog`
- Find a free port for the application on the production machine (you can usually bump the one from the config)

- On the production machine
- Update the DNS to add the new year 2024.rivieradev.fr and 2024.rivieradev.com in `/etc/bind/zones/rivieradev.fr` and `/etc/bind/zones/rivieradev.com`
- Restart the DNS `/etc/init.d/bind reload`
- Add the required folders with the proper permissions
	- `mkdir -p /www/2024.rivieradev.fr/htdocs /www/2024.rivieradev.fr/logs`
	- `chown -R www-data. /www/2024.rivieradev.fr`
	- `cp -r /www/2023.rivieradev.fr/data /www/2024.rivieradev.fr/`
	- `chown -R rivieradev. /www/2024.rivieradev.fr/data`
- Get new certificates for the new DNS from letsencrypt
	- `certbot certonly --apache -d 2024.rivieradev.fr,2024.rivieradev.com`
- Create the new DB
	- `sudo su - postgres`
	- `createdb -O rivieradev -E utf8 rivieradev-2024`
	- If it's the same DB you want to copy
		- `pg_dump rivieradev-2023 > rdev23.sql`
		- `psql rivieradev-2024`
		- `\i rdev23.sql`
	- If not, you can load the `db-X.sql` to get a blank DB
	- Or start from the previous DB and apply the `upgrade-X.sql` script
- Add the new apache entry in `/etc/apache/sites-enabled/rivieradev` by copying the previous year and changing the year

# License

The content of this repository is released under AGPLv3 as provided in
the LICENSE file that accompanied this code, with the following
clarifications.

The AGPL does not extend to the files in the public/ directory. Such
files are licensed as indicated in the file or else are dedicated to
the public domain to the maximum extent possible under applicable law.

The AGPL does not extend to any dependencies that we do not distribute
in our github repository or which are indicated in our github
repository as being under some other license, even if
AGPL-compatible. For example, if you create and publicly deploy a
modified version of Ceylon Herd that is based on the Play framework,
the AGPL does not extend to any of the dependencies that make up the
Play framework.

Compliance with the source code requirements of section 13 of AGPLv3
is satisfied by storing your modified version in a public revision
control repository and prominently providing your users with notice of
the location of this repository.
