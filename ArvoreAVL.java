import java.util.Random;

public class ArvoreAVL {

    static class No {
        int valor;
        int altura;
        No esq, dir;

        No(int valor) {
            this.valor = valor;
            this.altura = 1;
        }
    }

    static int altura(No no) {
        return (no == null) ? 0 : no.altura;
    }

    static int fatorBalanceamento(No no) {
        return (no == null) ? 0 : altura(no.esq) - altura(no.dir);
    }

    static void atualizarAltura(No no) {
        no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
    }

    static No rotacaoDireita(No y) {
        No x = y.esq;
        No T2 = x.dir;
        x.dir = y;
        y.esq = T2;
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }

    static No rotacaoEsquerda(No x) {
        No y = x.dir;
        No T2 = y.esq;
        y.esq = x;
        x.dir = T2;
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    static No inserir(No no, int valor) {
        if (no == null) return new No(valor);

        if (valor < no.valor)
            no.esq = inserir(no.esq, valor);
        else if (valor > no.valor)
            no.dir = inserir(no.dir, valor);
        else
            return no;

        atualizarAltura(no);
        int fb = fatorBalanceamento(no);

        if (fb > 1 && valor < no.esq.valor)
            return rotacaoDireita(no);

        if (fb < -1 && valor > no.dir.valor)
            return rotacaoEsquerda(no);

        if (fb > 1 && valor > no.esq.valor) {
            no.esq = rotacaoEsquerda(no.esq);
            return rotacaoDireita(no);
        }

        if (fb < -1 && valor < no.dir.valor) {
            no.dir = rotacaoDireita(no.dir);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    static No menorNo(No no) {
        No atual = no;
        while (atual.esq != null)
            atual = atual.esq;
            return atual;
    }

    static No remover(No no, int valor) {
        if (no == null) return null;

        if (valor < no.valor)
            no.esq = remover(no.esq, valor);
        else if (valor > no.valor)
            no.dir = remover(no.dir, valor);
        else {
            if (no.esq == null || no.dir == null) {
                no = (no.esq != null) ? no.esq : no.dir;
            } else {
                No sucessor = menorNo(no.dir);
                no.valor = sucessor.valor;
                no.dir = remover(no.dir, sucessor.valor);
            }
        }

        if (no == null) return null;

        atualizarAltura(no);
        int fb = fatorBalanceamento(no);

        if (fb > 1 && fatorBalanceamento(no.esq) >= 0)
            return rotacaoDireita(no);

        if (fb > 1 && fatorBalanceamento(no.esq) < 0) {
            no.esq = rotacaoEsquerda(no.esq);
            return rotacaoDireita(no);
        }

        if (fb < -1 && fatorBalanceamento(no.dir) <= 0)
            return rotacaoEsquerda(no);

        if (fb < -1 && fatorBalanceamento(no.dir) > 0) {
            no.dir = rotacaoDireita(no.dir);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    static boolean isAVL(No no) {
        if (no == null) return true;
        int fb = fatorBalanceamento(no);
        if (fb < -1 || fb > 1) return false;
        return isAVL(no.esq) && isAVL(no.dir);
    }

    static void imprimirEmOrdem(No no) {
        if (no == null) return;
        imprimirEmOrdem(no.esq);
        System.out.printf("Valor: %4d | FB: %2d%n", no.valor, fatorBalanceamento(no));
        imprimirEmOrdem(no.dir);
    }

    static int contarNos(No no) {
        if (no == null) return 0;
        return 1 + contarNos(no.esq) + contarNos(no.dir);
    }

    public static void main(String[] args) {
        No raiz = null;
        Random random = new Random();

        int[] numeros = new int[100];
        int count = 0;
        while (count < 100) {
            int n = random.nextInt(1001) - 500;
            boolean existe = false;
            for (int i = 0; i < count; i++) {
                if (numeros[i] == n) { existe = true; break; }
            }
            if (!existe) {
                numeros[count++] = n;
                raiz = inserir(raiz, n);
            }
        }

        System.out.println("==================================================");
        System.out.println("FASE 1: Apos inserir 100 numeros");
        System.out.println("==================================================");
        System.out.println("Total de nos: " + contarNos(raiz));
        System.out.println("E AVL valida? " + isAVL(raiz));
        System.out.println();
        System.out.println("Nos em ordem (valor | fator de balanceamento):");
        imprimirEmOrdem(raiz);

        System.out.println();
        System.out.println("==================================================");
        System.out.println("Removendo 20 numeros...");
        System.out.println("==================================================");
        for (int i = 0; i < 20; i++) {
            System.out.println("Removendo: " + numeros[i]);
            raiz = remover(raiz, numeros[i]);
        }

        System.out.println();
        System.out.println("==================================================");
        System.out.println("FASE 2: Apos remover 20 numeros");
        System.out.println("==================================================");
        System.out.println("Total de nos: " + contarNos(raiz));
        System.out.println("E AVL valida? " + isAVL(raiz));
        System.out.println();
        System.out.println("Nos em ordem (valor | fator de balanceamento):");
        imprimirEmOrdem(raiz);
    }
}
