import { useState } from 'react'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { createProject } from '../api/projects'
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { Key, Copy, Check } from 'lucide-react'

interface CreateProjectDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function CreateProjectDialog({ open, onOpenChange }: CreateProjectDialogProps) {
  const queryClient = useQueryClient()
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [apiKey, setApiKey] = useState<string | null>(null)
  const [copied, setCopied] = useState(false)

  const mutation = useMutation({
    mutationFn: () => createProject(name.trim(), description.trim()),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['projects'] })
      setApiKey(data.apiKey)
    },
  })

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!name.trim()) return
    mutation.mutate()
  }

  const handleCopy = () => {
    if (!apiKey) return
    navigator.clipboard.writeText(apiKey)
    setCopied(true)
    setTimeout(() => setCopied(false), 2000)
  }

  const handleClose = () => {
    setName('')
    setDescription('')
    setApiKey(null)
    setCopied(false)
    mutation.reset()
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={handleClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Create New Project</DialogTitle>
        </DialogHeader>

        {!apiKey ? (
          <form onSubmit={handleSubmit} className="flex flex-col gap-4 py-2">
            <div className="flex flex-col gap-1.5">
              <Label htmlFor="project-name">Project Name *</Label>
              <Input
                id="project-name"
                placeholder="My Project"
                value={name}
                onChange={(e) => setName(e.target.value)}
                disabled={mutation.isPending}
                autoFocus
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <Label htmlFor="project-description">Description</Label>
              <Textarea
                id="project-description"
                placeholder="A short description of the project..."
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                disabled={mutation.isPending}
                rows={3}
              />
            </div>

            {mutation.isError && (
              <p className="text-sm text-destructive">
                Failed to create project. Please try again.
              </p>
            )}

            <DialogFooter className="pt-2">
              <Button type="button" variant="ghost" onClick={handleClose} disabled={mutation.isPending}>
                Cancel
              </Button>
              <Button type="submit" disabled={!name.trim() || mutation.isPending}>
                {mutation.isPending ? 'Creating...' : 'Create Project'}
              </Button>
            </DialogFooter>
          </form>
        ) : (
          <div className="flex flex-col gap-4 py-2">
            <Alert>
              <Key className="h-4 w-4" />
              <AlertDescription>
                <p className="font-medium mb-1">Project created successfully!</p>
                <p className="text-xs text-muted-foreground">
                  Save your API key — it won't be shown again.
                </p>
              </AlertDescription>
            </Alert>

            <div className="flex flex-col gap-1.5">
              <Label>API Key</Label>
              <div className="flex items-center gap-2">
                <code className="flex-1 rounded-md bg-muted px-3 py-2 text-xs font-mono break-all">
                  {apiKey}
                </code>
                <Button
                  type="button"
                  variant="outline"
                  size="icon"
                  onClick={handleCopy}
                  className="shrink-0"
                >
                  {copied ? <Check className="h-4 w-4 text-green-500" /> : <Copy className="h-4 w-4" />}
                </Button>
              </div>
            </div>

            <DialogFooter className="pt-2">
              <Button onClick={handleClose}>Done</Button>
            </DialogFooter>
          </div>
        )}
      </DialogContent>
    </Dialog>
  )
}
