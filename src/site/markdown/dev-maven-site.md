## Creating Maven Site for Joala

Developing the Maven Site posed some problems as it is not a piece of cake to provide a multi module Maven Site which:

* renders relative links correctly,
* does not need many boilerplate copies of `site.xml`,
* ensures that the Project Info Module Report renders correct links,
* ensures that the parent and modules menu in `site.xml` works correctly,
* which deploys correctly as [GitHub Pages][joala-gh-pages].

## References

* akquinet.de: [Maven Sites Reloaded][akquinet-20120412]
* GitHub.com: [Site Maven Plugin][site-maven-plugin]
* stackoverflow.com: [How to create Maven Site for Multi Module][stackoverflow-multi-module-site]
* tech-mind: [Configure site plugin for Maven multi-project][techmind-multi-module-site]

<!-- Links -->

[joala-gh-pages]: <http://coremedia.github.com/joala> "GitHub hosted Pages for Joala"
[akquinet-20120412]: <http://blog.akquinet.de/2012/04/12/maven-sites-reloaded/> "2012-04-12: Maven Sites Reloaded at akquinet.de"
[site-maven-plugin]: <https://github.com/github/maven-plugins/tree/master/github-site-plugin> "GitHub.com: Maven Plugin to upload Maven Sites as GitHub Pages"
[stackoverflow-multi-module-site]: <http://stackoverflow.com/questions/5976123/maven-site-for-multi-module> "2011-05-13, Stackoverflow.com: How to create Maven Site for Multi Module"
[techmind-multi-module-site]: <http://tech-mind.blogspot.de/2009/01/configure-site-plugin-for-maven-multi.html> "2009-01-09: Configure site plugin for Maven multi-project"
