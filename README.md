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
