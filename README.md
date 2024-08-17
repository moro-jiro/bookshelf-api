# プロジェクト概要
このプロジェクトは、書籍と著者の情報を管理するための書籍管理システムです。本システムは、ユーザーが書籍および著者に関する情報を効率的に登録、変更、検索できる機能を提供します。

## 主な機能（Introduction）
- 書籍の登録: 書籍のタイトル、著者、出版日、出版社などの情報を登録できます。
- 著者の登録: 著者の名前、生年月日、性別などの情報を登録できます。
- 書籍および著者の検索: 登録された書籍および著者の情報を検索することができます。
- 書籍および著者の情報変更: 登録済みの書籍および著者の情報を更新することができます。

## セットアップ方法（Installation）
このプロジェクトをローカル環境でセットアップするには、以下の手順を実行してください

1. リポジトリをクローンします。

2. Docker を使用して PostgreSQL コンテナイメージを起動します。

```bash
docker-compose up
```

3. BookshelfApiApplication クラスの main 関数を実行し、アプリケーションを起動します。
4. アプリケーションは localhost:8080 で動作します。
5. データベースの接続情報については、compose.yaml ファイルを確認してください。

##  使用方法（Usage）
アプリケーション起動後、以下のエンドポイントにリクエストを送ることで各機能を確認できます。

### 書籍関連

書籍関連のエンドポイントは `BookController` クラスに記載されており、URLのコンテキストパスは以下の通りです：

- **ベースURL**: `http://localhost:8080/api/books`

#### 1. 書籍の登録

- **エンドポイント**: `POST http://localhost:8080/api/books/register`
- **リクエスト例**:

  ```json
  {
    "title": "サンプルブック3",
    "author": {
      "firstName": "太郎",
      "lastName": "山田",
      "birthDate": "1980-01-01",
      "gender": "MALE"
    },
    "publicationDate": "2023-08-02",
    "publisher": "サンプル出版社"
  }
  ```
  説明: 書籍は著者の情報と紐づくため、必ず著者の情報も含める必要があります。著者が既に登録されている場合は、そのレコードを再利用します。存在しない場合は、新たに登録します。著者の判定には、生年月日も使用されます。

#### 2. 書籍の更新

- **エンドポイント**: `PUT http://localhost:8080/api/books/{id}`
- **リクエスト例**:

  ```json
  {
  "title": "更新後の本のタイトル",
  "publicationDate": "2024-08-17",
  "publisher": "更新後の出版社名"
  }
  ```
  説明: 書籍のタイトル、出版日、出版社の情報を更新します。リクエストパラメーターに含まれるIDに紐づいた書籍が更新されます。

#### 3. 書籍の検索

- **エンドポイント**: `GET http://localhost:8080/api/books/search?title=サンプル`

  説明: リクエストパラメーターに含まれる書籍名に紐づく書籍のリストを取得できます。

### 著者関連

著者関連のエンドポイントは `AuthorController` クラスに記載されており、URLのコンテキストパスは以下の通りです：

- **ベースURL**: `http://localhost:8080/api/authors`

#### 1. 著者の登録

- **エンドポイント**: `POST http://localhost:8080/api/authors/register`
- **リクエスト例**:

  ```json
  {
  "firstName": "太郎",
  "lastName": "山田",
  "birthDate": "1990-01-01",
  "gender": "MALE"
  }
  ```
  説明: 著者は名字、名前、生年月日でユニークとなります。同じ著者が既に存在する場合はエラーが発生します。

#### 2. 著者の更新

- **エンドポイント**: `PUT http://localhost:8080/api/authors/{id}`
- **リクエスト例**:

  ```json
  {
  "firstName": "更新後の名",
  "lastName": "更新後の姓",
  "birthDate": "1985-07-15",
  "gender": "MALE"
  }
  ```
  説明: 著者の名字、名前、生年月日、性別の情報を更新します。リクエストパラメーターに含まれるIDに紐づいた著者が更新されます。

#### 3. 著者の検索

- **エンドポイント**: `GET http://localhost:8080/api/authors/search?lastName=山田&firstName=太郎&gender=MALE&birthDate=1980-01-01`

  説明: 著者の名字、名前、生年月日、性別の情報を更新します。リクエストパラメーターに含まれるIDに紐づいた著者が更新されます。

#### 4. 著者に紐づく書籍の一覧取得

- **エンドポイント**: `GET http://localhost:8080/api/authors/{id}/books`

  説明: 指定した著者IDに紐づく書籍のリストを取得します。IDを使用した検索であり、特定の著者に関連する書籍を効率的に取得できます。

##  構成（Project Structure)

このプロジェクトは以下のようなディレクトリ構造で構成されています：

  ```
.
├── BookshelfApiApplication.kt
├── application
│   ├── dto
│   │   ├── AuthorDto.kt
│   │   └── BookDto.kt
│   └── service
│       ├── AuthorService.kt
│       └── BookService.kt
├── domain
│   ├── model
│   │   ├── Author.kt
│   │   └── Book.kt
│   └── repository
│       ├── AuthorRepository.kt
│       └── BookRepository.kt
├── infrastructure
│   └── jooq
│       ├── generated
│       │   ├── DefaultCatalog.java
│       │   ├── Indexes.java
│       │   ├── Keys.java
│       │   ├── Public.java
│       │   ├── Tables.java
│       │   └── tables
│       │       ├── Author.java
│       │       ├── Book.java
│       │       ├── daos
│       │       │   ├── AuthorDao.java
│       │       │   └── BookDao.java
│       │       ├── pojos
│       │       │   ├── Author.java
│       │       │   └── Book.java
│       │       └── records
│       │           ├── AuthorRecord.java
│       │           └── BookRecord.java
│       └── repository
│           ├── JooqAuthorRepository.kt
│           └── JooqBookRepository.kt
└── presentation
    └── controller
        ├── AuthorController.kt
        └── BookController.kt

  ```
### 構成説明

- **BookshelfApiApplication.kt**: アプリケーションのエントリーポイントです。Spring Boot アプリケーションの設定や起動がここで行われます。

- **application**
    - **dto**: データ転送オブジェクト（DTO）が含まれます。これらは、サービス層とプレゼンテーション層の間でデータをやり取りするためのオブジェクトです。
        - `AuthorDto.kt`: 著者に関するデータをやり取りするためのDTO。
        - `BookDto.kt`: 書籍に関するデータをやり取りするためのDTO。
    - **service**: ビジネスロジックを処理するサービス層です。
        - `AuthorService.kt`: 著者に関するビジネスロジックを処理します。
        - `BookService.kt`: 書籍に関するビジネスロジックを処理します。

- **domain**
    - **model**: ドメインモデルが含まれます。アプリケーションの中心となるビジネス概念を表現します。
        - `Author.kt`: 著者を表すドメインモデル。
        - `Book.kt`: 書籍を表すドメインモデル。
    - **repository**: データベースとのやり取りを担当するリポジトリ層です。
        - `AuthorRepository.kt`: 著者に関するデータ操作を提供します。
        - `BookRepository.kt`: 書籍に関するデータ操作を提供します。

- **infrastructure**
    - **jooq**: jOOQによって生成されたコードが含まれます。データベースのスキーマに基づいて自動生成されたクラスやDAOがここに配置されます。
        - **generated**: jOOQによって自動生成されたコードが格納されています。
            - `DefaultCatalog.java`, `Indexes.java`, `Keys.java`, `Public.java`, `Tables.java`: データベースのテーブルやキーなどの定義が含まれます。
            - **tables**: データベースの各テーブルに対応するクラスが含まれています。
                - `Author.java`, `Book.java`: 著者および書籍テーブルに対応するクラス。
                - **daos**: データアクセスオブジェクト（DAO）が含まれます。
                    - `AuthorDao.java`, `BookDao.java`: 著者および書籍に対するデータアクセス操作を提供します。
                - **pojos**: シンプルなJavaオブジェクト（POJO）が含まれます。
                    - `Author.java`, `Book.java`: 著者および書籍のデータを保持するPOJOクラス。
                - **records**: レコードクラスが含まれます。
                    - `AuthorRecord.java`, `BookRecord.java`: 著者および書籍テーブルのレコードを表すクラス。
        - **repository**: jOOQを使用してデータ操作を行うリポジトリクラスが含まれます。
            - `JooqAuthorRepository.kt`: jOOQを用いた著者のデータ操作を提供します。
            - `JooqBookRepository.kt`: jOOQを用いた書籍のデータ操作を提供します。

- **presentation**
    - **controller**: プレゼンテーション層に属し、HTTPリクエストを処理するコントローラが含まれます。
        - `AuthorController.kt`: 著者に関するAPIリクエストを処理します。
        - `BookController.kt`: 書籍に関するAPIリクエストを処理します。

### DBの構成

このプロジェクトで使用されるデータベースのテーブル構造は以下の通りです。

#### テーブル: `author`

```sql
CREATE TABLE author
(
    id         SERIAL PRIMARY KEY,
    last_name  VARCHAR(100)                                              NOT NULL,
    first_name VARCHAR(100)                                              NOT NULL,
    birth_date DATE                                                      NOT NULL,
    gender     VARCHAR(10) CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (last_name, first_name, birth_date)
);
```

- インデックス:
  - `idx_author_last_name` : `last_name` 列にインデックスを作成します。
  - `idx_author_first_name` : `first_name` 列にインデックスを作成します。 
  - `idx_author_full_name` : `last_name` と `first_name` の組み合わせにインデックスを作成します。 
  - `idx_author_birth_date`: `birth_date` 列にインデックスを作成します。

#### テーブル: `book`

```sql
CREATE TABLE book
(
    id               SERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    publication_date DATE         NOT NULL,
    author_id        INT          NOT NULL,
    publisher        VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES author (id)
);
```

- インデックス:
    - `idx_books_title` : `title` 列にインデックスを作成します。

## テスト（Testing）

本プロジェクトでは、以下のテストを実施しています：

- **ビジネスロジックのユニットテスト**: アプリケーションのビジネスロジックをテストしています。jOOQを利用しており、それらを直接ビジネスロジックに組み込むと複雑になるため、Repositoryのインターフェイスを作成し、ビジネスロジックとデータアクセス層を分離しました。これにより、ビジネスロジックのテストを簡易化し、テストの実施が容易になっています。
- **Controller層のテスト**: POSTMANを利用して実際にサーバーを立ち上げそこに向けてAPIを叩いて検証、これによりバグを発見でき潰すことができました。その際にでた反省点を次項でまとめています。

## 反省点

本プロジェクトの開発中に、いくつかの課題に直面しました。

- **Entityのマッピング**: 自身が作成したEntityをデータアクセス層で処理する際に、適切にマッピングできないことがありました。(AuthorはできてBookはできない状態です)この問題により、かなりの時間を費やしてしまいました。(手動でマッピングすることで解決)

- **jOOQの理解不足**: jOOQに関する理解が不十分であったため、この問題を完全には解決できませんでした。その結果、jOOQの自動生成コードを利用しただけで、テストの優先度を下げてしまったことが間違いだったと感じています。今後は、テストの優先度を適切に保ち、理解不足による問題の再発を防ぎたいと考えています。

これらの反省点を踏まえ、今後の開発においては技術的な理解をさらに深め、効率的な問題解決を目指していきます。

