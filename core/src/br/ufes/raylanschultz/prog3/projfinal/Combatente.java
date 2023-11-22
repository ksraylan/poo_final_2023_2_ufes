package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Combatente extends Inimigo {
    public Combatente(Sprite sprite, Vector2 position, Vector2 hitbox, float vida, float velocidadeRotacao, Sprite[] destruicao, float atrito, float aceleracao, float velocidadeMaxima) {
        super(sprite, position, hitbox, vida, velocidadeRotacao, destruicao, atrito, aceleracao, velocidadeMaxima);
    }

    public void atirar(NaveAliada naveAliada) {
        if (!naveAliada.estaDestruido()) arma.atirar();
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        if (estaDestruido()) return;
        if (getPodeMoverCimaJogo())
            setMovimento(new Vector2(0, -1));
        else setMovimento(new Vector2(MathUtils.random(-1, 1), MathUtils.random(-1, 1)).nor());
        super.atualizarFisica(deltaTime);
    }
}
