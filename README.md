# IOMawaba

![Logo](https://www.interordi.com/images/plugins/iomawaba-96.png)

**Mawaba**: **Ma**nage **wa**rnings and **ba**ns for players on a Minecraft server.

This tool is designed to allow a server or network's staff members to handle the community. Warnings, kicks and bans are included in these measures. The plugin works in dual mode: it can be installed on both Bungee and Spigot (or equivalents) to provide various operations. For the best results, install it on both.

The kicks and bans commands were designed to reuse the syntax of [Bungee Admin Tools](https://github.com/alphartdev/BungeeAdminTools) as the staff was already familiar with the syntax.

A MySQL database is required.


## Warnings

Warnings are meant to tell a player that they're violating the rules, using a method more direct than a simple chat message. The staff can enter  `/warning PLAYER [reason]` to tell someone to back off. If no reason is provided, a default explanation about griefing will appear.

A core concept is that the staff members don't have to know how many times someone has been warned: they can just `/warning` them over and over again and the effects will stack automatically.

Warnings older than six months aren't counted.

1. The first /warning shows a clear "Pay attention" message to the target and slows them down for 30 seconds to grab their attention.
2. The second warning does the same as the above, plus kicks them out after a delay.
3. The third warning completely stops the player, displays the message then kicks them as a final warning.
4. The fourth warning bans for 30 days.
5. The fifth warning gives a permanent ban.

If a warning has been given by mistake, `/warning PLAYER clear` will remove the latest one, ensuring their standing is correctly set.


## Kicks and bans

You should know what to expect from these. Temporary and permanent options are offered, the full syntax is described below.


## Setup guide

1. Download the plugin and place it in the `plugins/` directory of the server / Bungee.
2. Start and stop the server / Bungee to create the configuration file.
3. Edit `plugins/IOMawaba/config.yml` to set your settings, described below.
5. Restart the server / Bungee.


## Wishlist

- [ ] Implement the kicks and bans commands at the server level for standalone use.
- [ ] Implement the warnings at the Bungee level to avoid requiring local installs.
- [ ] Allow the ban methods to apply to a single server on a network.
- [ ] Flexible notifications and warning effects.


## Configuration

`database.host`: Database host  
`database.port`: Database port  
`database.base`: Database name  
`database.username`: Database username  
`database.password`: Database password  
`bungee.use-broadcast`: Set to true if used in a Bungee environment  


## Commands

### Bungee

The "g" prefix stands for "global", as in the entire network. Any [reason] message is optional.

`/gkick PLAYER [reason]`: Kick a player from the network, with an optional message
`/gtempban PLAYER DURATION [reason]`: Temporarily ban a player from the network
`/gtempbanip IP DURATION [reason]`: Temporarily ban an IP address from the network
`/gban PLAYER [reason]`: Permanently ban a player from the network
`/gbanip IP [reason]`: Permanently ban an IP address from the network
`/gunban PLAYER`: Unban a player from the network
`/gunbanip IP`: Unban an IP address from the network 

**DURATION** for temporary bans supports a mix of any of the following:
* 7y or 7years: 7 years
* 6mo or 6months: 6 months
* 5w or 5week: 5 weeks
* 4d or 4days: 4 days
* 3h or 3hours: 3 hours
* 2m or 2mins: 2 minutes
* 1s or 1second: 1 second

Plural and singular forms can be used and mixed in any order. For example, **2y4mo** would ban for 2 years and 4 months. **3mins5day** would be 5 days and 3 minutes. **10s** would be 10 seconds.

### Server

`/warning PLAYER [reason]`: Issue a warning to the player  
`/warning PLAYER clear`: Clear the latest warning provided  
`/w`: Alias for `/warning`  


## Permissions

`iomawaba.admin`: Access to the full suite of commands  
`iomawaba.warning`: Allow the user to issue warnings or retract them  
