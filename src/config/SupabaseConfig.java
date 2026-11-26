package config;

/**
 * Classe utilitária de configuração que centraliza as credenciais e o endereço
 * de conexão da instância do Supabase utilizada pela camada de persistência.
 */
public final class SupabaseConfig {

    /**
     * URL base do endpoint REST do projeto no Supabase.
     */
    public static final String URL =
            "https://dicvxojuilstdgszzqbi.supabase.co";

    /**
     * Chave pública anônima ({@code anon key}) utilizada para autenticação das requisições HTTP na API.
     */
    public static final String ANON_KEY =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRpY3Z4b2p1aWxzdGRnc3p6cWJpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTAxMDE0OTQsImV4cCI6MjEwNTY3NzQ5NH0.Jx18_Yy5RcJNM5PHgm76H0UrqmoZ87KbtU3bP1V4OIk";

    private SupabaseConfig() {
    }
}
