using Microsoft.Extensions.Logging;

namespace Vigilant.Sdk;

public class VigilantConfig
{
    public string ServerUrl { get; set; } = string.Empty;
    public string ProjectId { get; set; } = string.Empty;
    public string ApiKey { get; set; } = string.Empty;
    public LogLevel MinLevel { get; set; } = LogLevel.Information;
}
