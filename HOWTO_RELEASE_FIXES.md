# How to Perform Bugfix Releases

If you ever observe that a release of Joala is broken it's time for a bugfix release. As mentioned
in [How to Release][HOWTO_RELEASE] it's time to build an increment version.

Let's assume that a serious bug got detected in release *0.2.0*. Now do the following steps:

1. Checkout the corresponding tag:

    ```
    joala$ git checkout joala-bom-0.2.0
    ```
2. Create a new fixes branch:

    ```
    joala$ git checkout -b joala-bom-0.2-fixes
    ```
3. Set the new version for the fixes release:

    ```
    joala$ mvn versions:set -DnewVersion=0.2.1-SNAPSHOT
    ```
4. Commit the changes:

    ```
    joala$ git commit -a -m "Set version for 0.2.0 fixes branch to 0.2.1-SNAPSHOT"
    ```

5. Do the fixes and commit the changes.

6. Push the changes and the branch (untested yet... perhaps might be skipped as we do the release locally anyway):

    ```
    joala$ git push origin joala-bom-0.2-fixes
    ```

7. Perform the release:

    ```
    joala$ mvn release:prepare release:perform
    ```

    As release version choose 0.2.1. As snapshot version choose 0.2.2-SNAPSHOT. That is
    both times just accept the default.

8. If a staging repository is used don't forget to close/release.

9. Push the release-changes:

    ```
    joala$ git push origin joala-bom-0.2-fixes --tags
    ```

10. Now start to move the fixes back to master:

    1. List the changes to merge:

        ```
        joala$ git log ..origin/joala-bom-0.2-fixes
        ```

    2. Assume that you have four changes:

        1. the first version change to 0.2.1-SNAPSHOT (hash: a123)
        2. the fix (hash: b234)
        3. the release commit (hash: c345)
        4. the *next development iteration* commit (hash: d456)

    3. Now ignore all changes on merge except the fix commit:

        ```
        joala$ git merge -s ours a123
        joala$ git merge b234
        joala$ git merge -s ours d456
        ```
    4. Push the changes to master:

        ```
        joala$ git push origin master
        ```

Next time you need to fix something on the 0.2-fixes branch you can of course skip the initial change
of the version.

<!-- Links -->

[HOWTO_RELEASE]: <./HOWTO_RELEASE.md> "How to Release"
