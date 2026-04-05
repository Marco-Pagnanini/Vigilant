using Microsoft.Extensions.Logging;

namespace Vigilant.Sdk;

public class VigilantLogger : ILogger
{
    private readonly VigilantConfig _config;
    private readonly VigilantHttpClient _httpClient;

    public VigilantLogger(VigilantConfig config, VigilantHttpClient httpClient)
    {
        _config = config;
        _httpClient = httpClient;
    }

    public IDisposable? BeginScope<TState>(TState state) where TState : notnull => null;

    public bool IsEnabled(LogLevel logLevel) => logLevel >= _config.MinLevel;

    public void Log<TState>(LogLevel logLevel, EventId eventId, TState state, Exception? exception, Func<TState, Exception?, string> formatter)
    {
        if (!IsEnabled(logLevel)) return;

        var message = formatter(state, exception);
        var level = logLevel.ToString().ToUpper();
        var stackTrace = exception?.ToString();

        Console.Error.WriteLine($"[Vigilant DEBUG] Sending log: {level} - {message}");

        Task.Run(async () => await _httpClient.SendLogAsync(level, message, stackTrace, _config));
    }
}
