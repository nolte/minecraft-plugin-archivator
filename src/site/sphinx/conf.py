# -*- coding: utf-8 -*-
import sys, os
from recommonmark.parser import CommonMarkParser

project = u'Minecraft Archivator'
copyright = u'YYYY, John Doe'
#version = '${project.version}'
#release = '${project.version}'

# General options
needs_sphinx = '1.0'
master_doc = 'index'
pygments_style = 'tango'
add_function_parentheses = True

extensions = [
    'sphinx.ext.autodoc',
    'sphinx.ext.doctest',
    'sphinx.ext.todo',
    'javasphinx',
    'sphinxcontrib.inlinesyntaxhighlight',
    'sphinxcontrib.httpdomain',
    'sphinx.ext.coverage',
    'sphinx.ext.viewcode',
    'sphinxcontrib.plantuml',
]

templates_path = ['_templates']
exclude_trees = ['.build']
source_suffix = ['.rst', '.md']
source_encoding = 'utf-8-sig'
source_parsers = {
  '.md': CommonMarkParser
}

# HTML options
html_theme = 'sphinx_rtd_theme'
html_short_title = "mc-archivator"
htmlhelp_basename = 'mc-archivator-doc'
html_use_index = True
html_show_sourcelink = False
html_static_path = ['_static']

# PlantUML options
plantuml = os.getenv('plantuml')
