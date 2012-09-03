# How to release Joala

Here are the recommended steps to release Joala:

```
joala/master$ git checkout -b local-release-<version>
joala/local-$ git release:prepare -B
joala/local-$ git release:perform -B
joala/local-$ git checkout master
joala/master$ git merge --log local-release-<version>

```