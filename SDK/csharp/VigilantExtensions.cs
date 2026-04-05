using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace Vigilant.Sdk;

public static class VigilantExtensions
{
    public static ILoggingBuilder AddVigilant(this ILoggingBuilder builder, Action<VigilantConfig> configure)
    {
        var config = new VigilantConfig();
        configure(config);

        builder.AddProvider(new VigilantLoggerProvider(config));
        builder.AddFilter<VigilantLoggerProvider>(null, LogLevel.Trace);

        return builder;
    }
}
