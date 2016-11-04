# NerdUsage

A port of Nerd.nu's usage tracker from CommandHelper to Java.

Player times are recorded in an SQLite database, and the results are periodically compiled into a JSON file written to the plugin's data directory. All I/O is handled on a separate thread for performance.
