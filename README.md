# rp-rest-subsystem-template

This is a template which should help setting up a subsystem using REST. A reference implementation would be the ONTA
Subsystem (https://git.int.compax.at/aax2rp/subsystems/onta-subsystem).

## TODOs

* copy the contents of this template in your new subsystem repository

### in your subsystem repository

* replace every occurrence of
    * `todo-rest-subsystem-name` and `rp-rest-subsystem-template` with the actual name of the new subsystem
    * `todorestsubsystemname` with the actual subsystem name; this should only affect the package name
    * `todoclientname` with the actual client name if the subsystem is client specific, otherwise just delete it; this should only affect the package name
    * in the `pom.xml` check for need and correct versions of snapshot package, plugins, dependencies etc.

### in Rancher

* create secrets for each stage separately (e.g. INT, TUA, REF, PROD)

### in database

* create database entries for each stage separately
    * `if_transaction_objects`
    * `if_transaction_object_settings`
    * `if_subsystems`
    * `s_cron_jobs` for sender and receiver jobs

### in DeployAll

* add entries in
    * `packagebuilder/config/aax2.json`
    * `packagebuilder/helm/aax-ew/values.yaml` in which the `enabled` attribute of the subsystem should be set to `false`; the `ew` in the directory is the
      project specific abbreviation
    * `packagebuilder/config/aax-ew-ewint-values.yaml` in which the `enabled` attribute of the subsystem should be set to `true`; the `ew` in the file name is
      the project specific abbreviation and `ewint` is the stage, there should be a `yaml` file for each project for each stage where you want to add the
      subsystem
    * validate the values with `scripts/validate-helm-values.sh`

### for release

* 
