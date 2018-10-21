# Echo
This is the project repository for our COMP30022 (IT Project) project.
It was built over 2018, Semester 2.

**Members:**
- Alexander Terp
- Lachlan Napoli
- Max Philip
- Jonas Olausson
- Samuel Xu


## How to build & run
0. Ensure [Git](https://git-scm.com/) and [Android Studio](https://developer.android.com/studio/) are installed on your computer.
1. Navigate to a directory where you would like to put the project.
2. Open a terminal and type in "git clone https://github.com/COMP30022-18/DropTable_Server/"
    - Git should clone the repository into its own folder.
3. Open up Android Studio.
4. There is a folder called "Echo" inside the repository you've downloaded. Open it in Android Studio as a project.
5. Ensure that Gradle is properly synced and has downloaded all the dependencies (see Gradle button in the top right of Android Studio).
6. Build the project by going into Build -> Make Project.
7. Set up an Android emulator if you have not already. Otherwise, [follow these instructions](https://developer.android.com/studio/run/emulator).
8. Run with the emulator within Android Studio. The instructions linked in the previous step also explain this.

Note: The project may complain about a missing class "ImmutableTask". If that happens, if means it has not been built correctly. Try the following.

1. Build -> Clean Project
2. File -> Invalidate Caches / Restart
3. Sync Gradle
4. Build -> Make Project or Rebuild Project.