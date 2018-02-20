# How to run the Riviera DEV web site

Note: at the moment it requires Play 1.2 which does not work on Java 8, so proceed with Java 7. We
are going to move it to Play 1.3 as soon as possible to fix that.

1. [Download Play Framework 1.2.7.2](https://downloads.typesafe.com/play/1.2.7.2/play-1.2.7.2.zip) and install it
1. Clone the [RivieraDEV repository](https://github.com/FroMage/RivieraDEV)
1. Open a shell and go to the `RivieraDEV` directory you just cloned
1. Create your Postgres DB (9.x)
    1. `sudo su - postgres`
    1. `createuser -PSRD rivieradev`
    1. _enter `rivieradev` as password when prompted_
    1. `createdb -O rivieradev -E utf8 rivieradev`
    1. exit
1. Run the application
    1. `play run`

# How to make your user (if registration is disabled, which is the default for now)

This can only be done by hand for now:

1. Pick a password
1. Open a `psql` console to your database:
    1. `psql -h localhost -U rivieradev`
1. Add your user (as admin)
    1. `INSERT INTO user_table  (id, firstname, lastname, password, username) VALUES ((select nextval('hibernate_sequence')), 'FirstName', 'LastName', 'Password', 'UserName');`
1. You can now log in and change your password using the UI

# How to compile SCSS files into CSS files

1. `sudo apt-get install ruby-full` # Install ruby
1. `gem install sass compass`       # Install compass
1. `npm install -g gulp`            # If you haven't installed gulp globally before
1. `npm install`                    # Install project dependencies
1. `gulp`                           # Compile and watch SCSS files

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



