using System.Text;
using System.Text.Json;

namespace Vigilant.Sdk;

public class VigilantHttpClient
{
    private static readonly HttpClient _httpClient = new();

    public async Task SendLogAsync(string level, string message, string? stackTrace, VigilantConfig config)
    {
        try
        {
            var body = new
            {
                level = MapLevel(level),
                message,
                timestamp = DateTime.UtcNow.ToString("yyyy-MM-ddTHH:mm:ss"),
                stackTrace
            };

            var json = JsonSerializer.Serialize(body);
            var content = new StringContent(json, Encoding.UTF8, "application/json");

            var request = new HttpRequestMessage(HttpMethod.Post, $"{config.ServerUrl}/api/logs/{config.ProjectId}")
            {
                Content = content
            };
            request.Headers.Add("X-API-Key", config.ApiKey);

            var response = await _httpClient.SendAsync(request);
            Console.Error.WriteLine($"[Vigilant DEBUG] Response: {response.StatusCode}");
        }
        catch (Exception ex)
        {
            Console.Error.WriteLine($"[Vigilant] Failed to send log: {ex.Message}");
        }
    }

    private static string MapLevel(string dotnetLevel) => dotnetLevel switch
    {
        "TRACE" => "INFO",
        "DEBUG" => "INFO",
        "INFORMATION" => "INFO",
        "WARNING" => "WARN",
        "ERROR" => "ERROR",
        "CRITICAL" => "ERROR",
        _ => "INFO"
    };
}
