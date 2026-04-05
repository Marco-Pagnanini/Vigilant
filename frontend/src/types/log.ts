export type LogLevel = 'INFO' | 'WARN' | 'ERROR' | 'SUCCESS'

export interface Log {
  id: string
  projectId: string
  level: LogLevel
  message: string
  timestamp: string
  stackTrace?: string
}
