# Receipt Processor Challenge

Solution to the test [receipt-processor-challenge](https://github.com/fetch-rewards/receipt-processor-challenge)

## Requirements 

1. [make](https://www.gnu.org/software/make/)

2. docker >= 18

3. docker-compose >= 1.25

## Use 

1. create the **.env** file with the environment variables in the root directory, take **.template.env** as an example

2. start project dependencies, this will raise a couple of docker images, one for postgresql and another for flyway, `./cicd/docker-compose.deps.yml` file is used:
    ```bash
    make start_dependencies
    ```
3. compile the project:
    ```bash
    make compile
    ```
4. run test
    1. start project dependencies:
        ```bash
        make start_dependencies
        ```
    2. run all tests:
        ```bash
        make test
        ```
    3. run only one class of tests:
        ```bash
       make test-only TEST_CLASS="CLASS-NAME"
        ```
        example:
        ```bash
       make test-only TEST_CLASS="com.dgg.receiptprocessorchallenge.ValidationsSpec"
        ```
    4. finally stop dependencies:
        ```bash
        make stop_dependencies
        ```
5. run sbt console inside docker, [sbt](https://www.scala-sbt.org/) is the tool used to build a scala application, `./cicd/docker-compose.dev.yml` file is used
    1. run sbt:
        ```bash
        make sbt
        ```
    2. from the console you can also compile and run tests
        ```bash
        sbt:receipt-processor-challenge> compile
        ```
        ```bash
        sbt:receipt-processor-challenge> test
        ```
        ```bash
        sbt:receipt-processor-challenge> testOnly com.dgg.receiptprocessorchallenge.persistance.postgres.QueriesSpec
        ```
    3. exit with `ctrl + c`
    4. finally stop dependencies:
        ```bash
        make stop_dependencies
        ```
6. run project:
    1. start run start dependencies and build the project using the file `./cicd/docker-compose.run.yml`:
        ```bash
        make start_run
        ```
    2. logs can be viewed using, exit with `ctrl + c`:
        ```bash
        docker logs -f main-sbt-1
        ```
        ![logs](./assets/logs)
    3. requests can be made to `http://localhost:8080/receipts/process
        ![request](./assets/requestpost)
        ![request](./assets/requestget)
    4. finally stop run:
        ```bash
        make stop_all
        ```
**Important.- stop and start the dependencies if a branch change is made**
for development, enable git hook to check format when committing
```bash
git config core.hooksPath .git-hooks
```
ff the format is wrong you can use:
```bash
make format
```
format can be checked at any time with:
```bash
make checkFormat
```