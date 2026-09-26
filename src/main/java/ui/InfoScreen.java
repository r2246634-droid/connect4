package ui;

import core.constants.Config;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import util.UiHelper;

/**
 * InfoScreen — Oyun kuralları ekranı.
 *
 * <p>Connect 4'ün nasıl oynandığını ve nasıl kazanıldığını kısa bölümler
 * hâlinde anlatır. Diğer ekranlarla aynı animasyonlu arka planı kullanır.
 */
public class InfoScreen extends StackPane {

    private final Connect4App app;
    private final AnimatedBackground bg;

    public InfoScreen(Connect4App app) {
        this.app = app;
        UiHelper.makeFillParent(this);
        bg = new AnimatedBackground(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        UiHelper.bindBackground(bg, this);
        getChildren().addAll(bg, buildContent());
        bg.start();
    }

    /** Başlık, kural bölümleri ve geri bağlantısını oluşturur. */
    private VBox buildContent() {
        Label title = UiHelper.titleLabel("OYUN KURALLARI");

        Label howToPlay = section("Nasıl Oynanır:\n\n" +
                "1. Oyuncular sırayla disklerini tahtaya bırakır.\n" +
                "2. Diskler her zaman en alt boşluğa düşer.\n" +
                "3. Amaç, kendi renginizden 4 diski yatay, dikey veya çapraz olarak yan yana getirmektir.");

        Label howToWin = section("Nasıl Kazanılır:\n\n" +
                "Kendi renginizden dört diski yatay, dikey veya çapraz bir çizgi halinde " +
                "birleştiren ilk oyuncu oyunu kazanır. Tahta dolar ve kimse kazanamazsa maç berabere biter.");

        Label back = UiHelper.backLink("← Ana Menü", () -> {
            bg.stop();
            app.showMainMenu();
        });

        VBox box = new VBox(20, title, howToPlay, howToWin, back);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        return box;
    }

    /** Yarı saydam panel içinde, satır kaydırmalı bir kural bölümü üretir. */
    private Label section(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Segoe UI", 17));
        lbl.setTextFill(Config.COLOR_TEXT);
        lbl.setWrapText(true);
        lbl.setMaxWidth(640);
        lbl.setPadding(new Insets(18, 22, 18, 22));
        lbl.setBackground(new Background(new BackgroundFill(
            Config.panelFill(), new CornerRadii(10), Insets.EMPTY)));
        return lbl;
    }
}
