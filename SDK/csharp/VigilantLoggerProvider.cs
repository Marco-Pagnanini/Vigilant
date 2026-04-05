using Microsoft.Extensions.Logging;

namespace Vigilant.Sdk;

public class VigilantLoggerProvider : ILoggerProvider
{
    private readonly VigilantConfig _config;
    private readonly VigilantHttpClient _httpClient;

    public VigilantLoggerProvider(VigilantConfig config)
    {
        _config = config;
        _httpClient = new VigilantHttpClient();
    }

    public ILogger CreateLogger(string categoryName) => new VigilantLogger(_config, _httpClient);

    public void Dispose() { }
}
