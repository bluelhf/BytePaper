<img align="right" src="./styles/logo.svg" width="15%"></img>

# BytePaper
![GitHub release (latest SemVer including pre-releases)](https://img.shields.io/github/v/release/bluelhf/BytePaper?color=D2883E&include_prereleases&label=release)

BytePaper is a [ByteSkript](https://docs.byteskript.org "Scott, M. (2021). ByteSkript [Computer software]") library and [Paper](https://papermc.io) plugin that bridges the gap between the Minecraft server and the ByteSkript language. It allows you to compile and run ByteSkript scripts (.bsk) that interact with your Paper server, all from in-game commands.

### We're still improving!
ByteSkript is a fairly young project, and BytePaper even younger than that. As such, many [Skript](https://skriptlang.org) syntaxes may not be available in BytePaper. If there's one you'd like to see, leave a [feature request](https://github.com/bluelhf/BytePaper/issues/new?assignees=&labels=enhancement&template=feature_request.md&title=) and we'll get to work adding it ASAP!

## Usage
Using BytePaper is simple!
1. Download the .jar file, and place it in your server's `plugins` directory
2. (Re)start your server
3. BytePaper is now installed! You'll find that a new `scripts` directory has been created in the `plugins/BytePaper/` directory. This is the home of all of your future scripts.
4. Create a new file with a name ending in `.bsk` in the newly created directory. This is your first script!
5. Open the script file in your favourite text editor, and write the following code:
    ```haskell
   on script load:
     trigger:
       send raw "Hello, world!" to console
    ```
6. Join your server and run `/bp load <your file's name>`
7. Look at your server console, and notice the message you wrote!

### Coming from Skript?
If you've gotten used to the SkriptLang implementation of the Skript language, you've come to the right place! ByteSkript is like Skript, but it's compiled to Java bytecode, making it much faster! There are also some syntactical differences, but you'll get used to them quite quickly.
