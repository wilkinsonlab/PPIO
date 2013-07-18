#!/bin/sh

# ARGUMENTS:
# Ontology file path or resolvable IRI
# Base namespace for entity IRIs
# Alphanumeric part of fragment
# Number of digits

# E.G, this

java -jar owl_id_generator.jar PPIO.owl http://purl.oclc.org/PPIO# PPIO 6

# Will result in IDs of the type http://purl.oclc.org/PPIO#PPIO_000001












