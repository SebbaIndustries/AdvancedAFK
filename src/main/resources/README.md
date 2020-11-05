# AdvancedAFK
Player AFK module with advanced capabilities and lightweight game configuration based on the idea of DAY (Detect and Yeet). <br>
<br>
Version: `1.0-SNAPSHOT`<br>
Native API: `Paper-1.16.3-R0.1-SNAPSHOT`<br>
Source code: <a href="https://github.com/SebbaIndustries/AdvancedAFK">github.com/SebbaIndustries/AdvancedAFK</a><br>
Wiki: <a href="#">TODO: Plugin Wiki here</a><br>
Developer: `SebbaIndustries` <br>


[![license](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/mit-license.php)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/SebbaIndustries/AdvancedAFK/Java%20CI%20with%20Maven/master)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/SebbaIndustries/AdvancedAFK/CodeQL/master?label=codeQL)
[![GitHub issues](https://img.shields.io/github/issues/SebbaIndustries/Warps)](https://github.com/SebbaIndustries/AdvancedAFK/issues)

How To (Server Owners)
------
This is a plugin build on PaperAPI, you can run it on Spigot but use Paper for best experience.<br>

Download AdvancedAFK here: <a href="#">TODO setup download site</a>

<b>Installation:</b> 
- Place AdvancedAFK-VERSION.jar (`AdvancedAFK-v0.1.6.jar`) file into plugins folder
- Start the server, plugin will generate `AdvancedAFK` directory with files:
  * `README.md`
  * `configuration.properties`
  * `messages.properties`
- Stop the server after everything has been loaded
- Open and configure the plugin to your needs.
- Start the server and enjoy the plugin!

<b>Message configuration:</b>
<br>All messages are stored in `messages.properites`, you can change them to your liking.


## Features

Todo: Features here

## Commands & permissions
#### `/aafk`
Use:
- `/aafk` and `/aafk help` `/`
<br>This command shows help message.

- `/aafk list` `aafk.list`
<br>This command lists all AFK players.
  
<br>

- `/aafk clean` `aafk.clean`
<br>This command removes all afk players from the server.
<br>Optional flags:
  - `--force` removes all AFK players (permissions are ignored).
  
<br>

- `/aafk lookup <player>` `aafk.lookup`
<br>This command provides detailed info about the player.
<br>Optional flags:
  - `-v` provides more verbose information.
  
<br>

- `/aafk reload` `aafk.reload`
<br>This is command reloads the plugin

#### `Actions`
Kick bypass: `aafk.bypass.kick` <br>