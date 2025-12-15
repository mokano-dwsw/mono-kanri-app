# ------------------------------------
# 1. ビルドステージ: アプリケーションのコンパイルとパッケージング
# ------------------------------------
# Java 17のMavenイメージを使用
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# 作業ディレクトリを設定
WORKDIR /app

# Mavenプロジェクトファイルをコピー
COPY pom.xml .
# 依存関係をダウンロードしてキャッシュ (ビルド高速化のため)
RUN mvn dependency:go-offline

# すべてのソースコードをコピー
COPY src ./src

# アプリケーションをビルドし、実行可能なjarファイルを生成
RUN mvn clean install -DskipTests

# ------------------------------------
# 2. 実行ステージ: アプリケーションの実行
# ------------------------------------
# 軽量なJava 17 JREイメージを使用
FROM eclipse-temurin:17-jre-focal

# 環境変数: Renderが公開するポート
ENV PORT 10000

# 作業ディレクトリを設定
WORKDIR /app

# ビルドステージで生成したjarファイルをコピー
COPY --from=builder /app/target/mono-kanri-app-0.0.1-SNAPSHOT.jar ./app.jar

# 実行ファイルとして公開
EXPOSE 10000

# アプリケーションの起動コマンド
ENTRYPOINT ["java", "-jar", "app.jar"]
