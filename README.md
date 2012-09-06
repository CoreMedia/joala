# Joala GitHub Pages

These pages are reachable as [Joala GitHub Pages][joala-gh-pages]. You can edit
the pages on the branch [gh-pages][joala-github-pages-source] at the [Joala GitHub Repository][joala-github].

Mind that while some of these pages are created manually others, located in the folder `site` are created by
the plugin `com.github.github:site-maven-plugin`. Thus they are auto-generated and at least for release versions
you should not delete them.

## Joala Sites

The Maven Site for Joala is uploaded to GitHub through the `site-maven-plugin` available as one of the
[GitHub Maven Plugins][github-maven-plugins] (introduced in a [blog post][blog-github-maven-plugins] in September 2011).

The site is generated to a sub-folder `site` on the pages site. In addition another folder denotes the version of
the published site. Expect SNAPSHOT-sites to vanish soon.

The configuration in the POMs is derived from a [blog post by Flaming Penguin][blog-flaming-penguin] which provides
a workaround for multi-module Maven projects.

<!-- Links -->


[joala-gh-pages]: <http://coremedia.github.com/joala/> "Joala GitHub Pages"
[joala-github]: <https://github.com/CoreMedia/joala> "Joala at GitHub"
[joala-github-pages-source]: <https://github.com/CoreMedia/joala/tree/gh-pages> "Joala GitHub Pages at GitHub"
[github-maven-plugins]: <https://github.com/github/maven-plugins> "GitHub Maven Plugins"
[blog-github-maven-plugins]: <https://github.com/blog/945-github-maven-plugins> "Blog Post: GitHub Maven Plugins"
[blog-flaming-penguin]: <http://www.flamingpenguin.co.uk/blog/2012/02/15/github-maven-plugins/> "Flaming Penguin: Github Maven Plugins"
