import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { fetchLogs } from '../api/logs'
import { Badge } from '@/components/ui/badge'
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table'
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
} from '@/components/ui/dialog'
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationNext,
    PaginationPrevious,
    PaginationEllipsis,
    PaginationLink,
} from '@/components/ui/pagination'
import type { Log, LogLevel } from '../types/log'

interface LogsTableProps {
    projectId: string
}

const levelVariant = (level: LogLevel) => {
    switch (level) {
        case 'ERROR': return 'destructive'
        case 'WARN': return 'outline'
        case 'SUCCESS': return 'outline'
        default: return 'secondary'
    }
}

const levelColor = (level: LogLevel) => {
    switch (level) {
        case 'ERROR': return 'text-red-500'
        case 'WARN': return 'text-yellow-500'
        case 'SUCCESS': return 'text-green-500'
        default: return 'text-blue-400'
    }
}

const PAGE_SIZE = 20

export const LogsTable = ({ projectId }: LogsTableProps) => {
    const [page, setPage] = useState(0)
    const [selectedLog, setSelectedLog] = useState<Log | null>(null)

    const { data, isLoading, isError } = useQuery({
        queryKey: ['logs', projectId, page],
        queryFn: () => fetchLogs(projectId, page, PAGE_SIZE),
        refetchInterval: 5000,
    })

    const totalPages = data?.totalPages ?? 1

    const getPageNumbers = () => {
        if (totalPages <= 5) return Array.from({ length: totalPages }, (_, i) => i)
        if (page <= 2) return [0, 1, 2, 3, null, totalPages - 1]
        if (page >= totalPages - 3) return [0, null, totalPages - 4, totalPages - 3, totalPages - 2, totalPages - 1]
        return [0, null, page - 1, page, page + 1, null, totalPages - 1]
    }

    if (isLoading) return <p className="text-sm text-muted-foreground">Loading logs...</p>
    if (isError) return <p className="text-sm text-destructive">Failed to load logs.</p>
    if (!data?.content?.length) return <p className="text-sm text-muted-foreground">No logs found for this project.</p>

    return (
        <>
            <div className="rounded-lg border border-border overflow-hidden">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-24">Level</TableHead>
                            <TableHead>Message</TableHead>
                            <TableHead className="w-44">Timestamp</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {data.content.map(log => (
                            <TableRow
                                key={log.id}
                                className="hover:bg-muted/40 cursor-pointer"
                                onClick={() => setSelectedLog(log)}
                            >
                                <TableCell>
                                    <Badge variant={levelVariant(log.level)} className={`text-xs ${levelColor(log.level)}`}>
                                        {log.level}
                                    </Badge>
                                </TableCell>
                                <TableCell className="text-sm font-mono max-w-xl truncate">
                                    {log.message}
                                </TableCell>
                                <TableCell className="text-xs text-muted-foreground">
                                    {new Date(log.timestamp).toLocaleString()}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>

            {/* Pagination */}
            {totalPages > 1 && (
                <div className="flex items-center justify-between mt-4">
                    <p className="text-xs text-muted-foreground">
                        Page {page + 1} of {totalPages} · {data.totalElements} total logs
                    </p>
                    <Pagination>
                        <PaginationContent>
                            <PaginationItem>
                                <PaginationPrevious
                                    onClick={() => setPage(p => Math.max(0, p - 1))}
                                    aria-disabled={page === 0}
                                    className={page === 0 ? 'pointer-events-none opacity-50' : 'cursor-pointer'}
                                />
                            </PaginationItem>

                            {getPageNumbers().map((p, i) =>
                                p === null ? (
                                    <PaginationItem key={`ellipsis-${i}`}>
                                        <PaginationEllipsis />
                                    </PaginationItem>
                                ) : (
                                    <PaginationItem key={p}>
                                        <PaginationLink
                                            isActive={p === page}
                                            onClick={() => setPage(p)}
                                            className="cursor-pointer"
                                        >
                                            {p + 1}
                                        </PaginationLink>
                                    </PaginationItem>
                                )
                            )}

                            <PaginationItem>
                                <PaginationNext
                                    onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                                    aria-disabled={page === totalPages - 1}
                                    className={page === totalPages - 1 ? 'pointer-events-none opacity-50' : 'cursor-pointer'}
                                />
                            </PaginationItem>
                        </PaginationContent>
                    </Pagination>
                </div>
            )}

            {/* Log detail dialog */}
            <Dialog open={!!selectedLog} onOpenChange={(open) => !open && setSelectedLog(null)}>
                <DialogContent className="max-w-4xl w-full">
                    <DialogHeader>
                        <DialogTitle className="flex items-center gap-3">
                            <Badge
                                variant={selectedLog ? levelVariant(selectedLog.level) : 'secondary'}
                                className={`text-sm px-3 py-1 ${selectedLog ? levelColor(selectedLog.level) : ''}`}
                            >
                                {selectedLog?.level}
                            </Badge>
                            <span className="text-sm text-muted-foreground font-normal">
                                {selectedLog && new Date(selectedLog.timestamp).toLocaleString()}
                            </span>
                        </DialogTitle>
                    </DialogHeader>

                    <div className="space-y-5 mt-2">
                        <div>
                            <p className="text-xs text-muted-foreground mb-2 uppercase tracking-widest font-medium">Message</p>
                            <pre className="text-sm font-mono bg-muted rounded-lg p-4 whitespace-pre-wrap break-all leading-relaxed min-h-[80px]">
                                {selectedLog?.message}
                            </pre>
                        </div>

                        {selectedLog?.stackTrace && (
                            <div>
                                <p className="text-xs text-muted-foreground mb-2 uppercase tracking-widest font-medium">Stack Trace</p>
                                <pre className="text-xs font-mono bg-muted rounded-lg p-4 whitespace-pre-wrap break-all max-h-80 overflow-y-auto text-destructive leading-relaxed">
                                    {selectedLog.stackTrace}
                                </pre>
                            </div>
                        )}
                    </div>
                </DialogContent>
            </Dialog>
        </>
    )
}
