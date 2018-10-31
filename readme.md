# Archivator

## Commands

| Command               | Example                 |                     Description |
|-----------------------|-------------------------|---------------------------------|
| ```/backup [world]``` | ```/backup world```     | Manual start the Backup Process |
| ```/backuplist```     | ```/backuplist```       | List all existing backups       |


## Installation

### Configuration

By default the ```Archivator``` using the Pluginfolder for archive the Backuops.

*example config:*
```
archivator:
  archive:
    dir: Archivator/archive
    name: "mc-{0,DATE,YYYMMDDHHmmSSsss}"
    format: zip
  workingtmp: Archivator/workdir
  strategy: safty
  report:
    enabled: true
  sources:
    worlds: *
  database:
    type: sqlite
    file: archivator.db
    table_prefix: "arc_"

```

### Permissions


## Features

- local Archiving the Backup
  - as ```*.tar.gz``` or ```*.zip```
  - configure the archived filename
- create a Markdown Backup Report, to the Archive Folder  
- starting the backup over [RCON](https://wiki.vg/RCON)


### Planed Feature Sets

- maximum keeped archives
- automatical backup job, controlled by Cron
- markdown Backup report
  - Publishing the report to a Markdown Based Wiki like [Golum Wiki](https://github.com/gollum/gollum)
- Ingame Reporting an History function
- publish the backup to external storage like FTP, DropBox, etc.
- restoring some Backup
  - from local storage
  - from external storage


## Used Java Stuff

- [Java-Markdown-Generator](https://github.com/Steppschuh/Java-Markdown-Generator)

## Links

### Build Plugins Link Collection
- [Bukkit PluginTutorial](https://bukkit.gamepedia.com/Plugin_Tutorial)
- [bukkit scheduling](https://bukkit.gamepedia.com/Scheduler_Programming)
- [color-chat](https://dev.bukkit.org/projects/color-chat)
- [sql light](https://www.spigotmc.org/threads/how-to-sqlite.56847/)


### Additonal Tools
- [RCon CommandLineClient](https://github.com/Kealper/Batchcraft)
