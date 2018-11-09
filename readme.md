# Archivator

The Next Minecraft Backup Plugin.

## External used Services

| Command               |                     Description |
|-----------------------|---------------------------------|
| [![CircleCI](https://circleci.com/gh/nolte/minecraft-plugin-archivator.svg?style=svg)](https://circleci.com/gh/nolte/minecraft-plugin-archivator) | [circleci.com](https://circleci.com) used for Release Management, and CI |
| [![Build Status](https://travis-ci.org/nolte/minecraft-plugin-archivator.svg?branch=develop)](https://travis-ci.org/nolte/minecraft-plugin-archivator) | [travis-ci.org](https://travis-ci.org) used for Plain CI  |
|[![CodeFactor](https://www.codefactor.io/repository/github/nolte/minecraft-plugin-archivator/badge/develop)](https://www.codefactor.io/repository/github/nolte/minecraft-plugin-archivator/overview/develop) | [codefactor.io](https://www.codefactor.io) used for static code analysing |
| [![Maintainability](https://api.codeclimate.com/v1/badges/695284e2c85bdbc781a9/maintainability)](https://codeclimate.com/github/nolte/minecraft-plugin-archivator/maintainability) | [codeclimate.com](https://codeclimate.com) using for static code analysing |
| [![Test Coverage](https://api.codeclimate.com/v1/badges/695284e2c85bdbc781a9/test_coverage)](https://codeclimate.com/github/nolte/minecraft-plugin-archivator/test_coverage) | present the [jacoco](https://www.jacoco.org/jacoco/) calculated Test Converage |

## Commands

| Command               | Example                 |                     Description |
|-----------------------|-------------------------|---------------------------------|
| ```/backup``` | ```/backup```     | Manual start the Backup Process |
| ```/backuplist```     | ```/backuplist```       | List all existing backups       |


## Installation

### Configuration

By default the ```Archivator``` using the Pluginfolder for archive the Backuops.

*example config:*
```
archivator:
  archive:
    dir: archive
    name: mc-{0,DATE,YYYMMDDHHmmSSsss}
    format: zip
  workingtmp: workdir
  strategy: safty
  maxBackupsBeforeErase: 5
  report:
    enabled: true
  sources:
    worlds: '*'
  database:
    type: sqlite
    file: archivator.db
    table_prefix: arc_

```

### Permissions


## Features

- local Archiving the Backup
  - as ```*.tar.gz``` or ```*.zip```
  - configure the archived filename
- create a Markdown Backup Report, to the Archive Folder  
- starting the backup over [RCON](https://wiki.vg/RCON)
- maximum keeped archives

### Planed Feature Sets

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
