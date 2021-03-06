On OSX 10.10 install posgresql using:
```
brew install postgres
```

-----
Notes:

If builds of PostgreSQL 9 are failing and you have version 8.x installed,
you may need to remove the previous version first. See:
  https://github.com/Homebrew/homebrew/issues/2510

To migrate existing data from a previous major version (pre-9.4) of PostgreSQL, see:
  http://www.postgresql.org/docs/9.4/static/upgrading.html

To have launchd start postgresql at login:
```
ln -sfv /usr/local/opt/postgresql/*.plist ~/Library/LaunchAgents
```
Then to load postgresql now:
```
launchctl load ~/Library/LaunchAgents/homebrew.mxcl.postgresql.plist
```
Or, if you don't want/need launchctl, you can just run:
```
postgres -D /usr/local/var/postgres
```

-----

Create a new database and edit it using:
```
createdb twister_dev
psql twister_dev
```