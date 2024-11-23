import java.io.*;
import java.util.*;

public class MochileiroInvestidor {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Scanner scannerString = new Scanner(System.in);
        System.out.println("Informe o arquivo de investimento (exemplo: investimentos10.txt): ");
        String filePath = scannerString.nextLine();
        System.out.println("Informe o Budget disponível: R$");

        int orcamento = scanner.nextInt();

        try {

            long tempoInicial = System.currentTimeMillis();

            List<Investimento> investimentos = carregarInvestimentos(filePath);

            List<Investimento> selecionados = melhoresInvestimentos(investimentos, orcamento);

            System.out.println("Investimentos Selecionados:");
            int totalCusto = 0;
            int totalRetorno = 0;

            for (Investimento inv : selecionados) {
                System.out.println("Nome: " + inv.nome + ", Custo: " + inv.custo + ", Retorno: " + inv.retorno);
                totalCusto += inv.custo;
                totalRetorno += inv.retorno;
            }

            System.out.println("\n===============================");
            System.out.println("Total a investir: R$ " + totalCusto);
            System.out.println("Retorno total esperado: R$ " + totalRetorno);


            long tempoFinal = System.currentTimeMillis();

            long tempoTotal = tempoFinal - tempoInicial;

            System.out.println("\nTempo de execução: " + tempoTotal + " ms");
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public static List<Investimento> melhoresInvestimentos(List<Investimento> investimentos, int orcamento) {
        int n = investimentos.size();
        int[][] dp = new int[n + 1][orcamento + 1];

        // Preenchendo a tabela dp usando a abordagem bottom-up
        for (int i = 1; i <= n; i++) {
            Investimento inv = investimentos.get(i - 1);
            for (int w = 0; w <= orcamento; w++) {
                if (inv.custo <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - inv.custo] + inv.retorno);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Recuperar os itens selecionados
        List<Investimento> selecionados = new ArrayList<>();
        int w = orcamento;
        for (int i = n; i > 0 && w > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Investimento inv = investimentos.get(i - 1);
                selecionados.add(inv);
                w -= inv.custo;
            }
        }
        return selecionados;
    }


    public static List<Investimento> carregarInvestimentos(String filePath) throws IOException {
        List<Investimento> investimentos = new ArrayList<>();
        InputStream inputStream = MochileiroInvestidor.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new FileNotFoundException("Arquivo não encontrado: " + filePath);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String linha;
        while ((linha = reader.readLine()) != null) {
            String[] partes = linha.split(" ");
            String nome = partes[0];
            int custo = Integer.parseInt(partes[1]);
            int retorno = Integer.parseInt(partes[2]);
            investimentos.add(new Investimento(nome, custo, retorno));
        }
        reader.close();
        return investimentos;
    }
}



