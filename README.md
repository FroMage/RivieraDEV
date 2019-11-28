# How to run the Riviera DEV web site

Note: at the moment it requires Play 1.5.3.

1. [Download Play Framework 1.5.3](https://downloads.typesafe.com/play/1.5.3/play-1.5.3.zip) and install it
1. Clone the [RivieraDEV repository](https://github.com/FroMage/RivieraDEV)
1. Download and install Postgres 9 or 11 on port 5433
1. Open a shell and go to the `RivieraDEV` directory you just cloned
1. Create your Postgres DB (9.x)
    - On Linux:
        1. `sudo su - postgres`
        2. `createuser -p 5433 -PSRD rivieradev`
        3. _enter `rivieradev` as password when prompted_
        4. `createdb -p 5433 -O rivieradev -E utf8 rivieradev-2020`
        5. exit
    - On Windows:
        1. `createuser -U postgres -p 5433 -PSRD rivieradev`
        2. _enter `rivieradev` as password when prompted_
        3. _enter the postgres password chosen during the installation when prompted_
        4. `createdb -U postgres -p 5433 -O rivieradev -E utf8 rivieradev-2020`
        5. _enter the postgres password chosen during the installation when prompted_
1. Download the module dependencies
    1. `play dependencies --forProd`
1. Run the application
    1. `play run`

# How to make your user (if registration is disabled, which is the default for now)

This can only be done by hand for now:

1. Pick a password
1. Open a `psql` console to your database:
    1. `psql -p 5433 -h localhost -U rivieradev rivieradev-2020`
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
-   `PROMOTED_PAGE_2`; It's the same as above but for the secondary button. The possible values are `SPONSORS` and `SCHEDULE`. And it's the same, let's not use it for now.
-   `TICKETING_URL`: At this time the ticketing is not opened yet, but if we already know the URL, we can fill it.
-   `TICKETING_OPEN = false`
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

-   `TICKETING_OPEN = false`
-   `PROMOTED_PAGE = SPONSORS`

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
