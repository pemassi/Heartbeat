# HeartBeat

![Gradle CI](https://github.com/pemassi/HeartBeat/actions/workflows/gradle.yml/badge.svg)

YAML-based Customizable Monitoring Application

## This project is still working on
I am still working on this project.

## What is this?
This project is for IT Operator. This application can monit end-point and alert external messenger when meets condition. You can make a rule easily with writing YAML file.

## Example Rule File
> More examples in `rules` folder.

```yaml
name: "Example Ping Rule"
description: "This is example."

cron: "0/5 * * ? * *"

test:
  ping:
    host: "localhost"
    timeout: 1000

condition:
  failMoreThan:
    times: 1

alert:
  title: TEST
  body: TEST
  console:
    debug: true

```
