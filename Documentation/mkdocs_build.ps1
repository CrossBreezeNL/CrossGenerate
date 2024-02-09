# The following command will build the HTML version of the documentation based on the current docs files.
# The output will be written in the 'site' folder.
docker run --rm -it -v ${PWD}:/docs squidfunk/mkdocs-material build
