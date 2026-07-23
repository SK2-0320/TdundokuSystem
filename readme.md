# 積読管理システム README

---

## ■ システム名

積読管理システム

---

## ■ 概要

本システムは、購入した書籍を管理し、「未読」「読書中」「読了」などの
積読状態を記録・管理するためのデスクトップアプリケーションです。

ISBNコードや書籍情報を登録し、検索・一覧表示・編集・削除・
積読ステータス変更・統計情報の表示を行うことができます。

---

## ■ 開発環境

開発言語：Java

開発環境：
・Visual Studio Code
・Extension Pack for Java
・JDK 17以上

---

## ■ ディレクトリ構成

src
├─ controller
│   ├─ BookRegisterController.java
│   ├─ BookSearchController.java
│   ├─ BookEditDeleteController.java
│   ├─ StatusChangeController.java
│   ├─ StatisticsController.java
│   └─ TsundokuPeriodController.java
│
├─ domain
│   ├─ Book.java
│   ├─ Bookshelf.java
│   └─ ReadingStatistics.java
│
├─ gui
│   ├─ MainView.java
│   ├─ BookRegisterView.java
│   ├─ BookSearchView.java
│   ├─ BookListView.java
│   ├─ BookStatusChangeView.java
│   └─ StatisticsView.java
│
├─ storage
│   └─ BookRepository.java
│
└─ Main.java

---

## ■ 主な機能

・書籍の登録
・書籍の検索
・書籍一覧表示
・書籍情報の編集
・書籍情報の削除
・積読ステータスの変更
・積読期間の計算
・統計情報の表示

---

## ■ 書籍情報

登録できる情報は以下のとおりです。

・ISBNコード
・書籍名
・著者名
・積読ステータス
・価格
・登録日
・購入日
・評価
・レビュー

---

## ■ 実行方法

1. プロジェクトをVisual Studio Codeで開く。

2. JDKがインストールされていることを確認する。

3. Main.javaを実行する。

4. メイン画面から各機能を利用する。

---

## ■ 操作方法

【書籍登録】
必要事項を入力して「登録」ボタンを押す。

【書籍検索】
検索キーワードを入力し、検索ボタンを押す。

【一覧表示】
登録されている書籍を一覧表示する。

【編集・削除】
一覧から対象書籍を選択して編集または削除する。

【ステータス変更】
未読・読書中・読了の状態を変更できる。

【統計情報】
登録冊数やステータス別冊数などを表示する。

---

## ■ 注意事項

・ISBN検索機能を利用する場合は、別途書籍情報取得APIとの接続実装が必要です。
・データ保存機能が未実装の場合、アプリケーション終了後は登録内容が保存されません。
・永続化を行う場合は、ファイル保存やデータベースとの連携を実装してください。

---

## ■ 作成者

bb38124040 古賀颯太

---

## ■ バージョン

Version 1.0
