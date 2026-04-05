import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { fetchProjects } from './api/projects'
import { ProjectCard } from './components/ProjectCard'
import { AppSidebar } from './components/AppSidebar'
import { LogsTable } from './components/LogsTable'
import { SidebarProvider, SidebarTrigger } from '@/components/ui/sidebar'
import { Separator } from '@/components/ui/separator'

function App() {
  const [selectedProjectId, setSelectedProjectId] = useState<string | null>(null)

  const { data: projects = [], isLoading, isError } = useQuery({
    queryKey: ['projects'],
    queryFn: fetchProjects,
    refetchInterval: 5000,
  })

  return (
    <SidebarProvider>
      <div className="flex min-h-screen w-full bg-background text-foreground">

        <AppSidebar
          projects={projects}
          selectedProjectId={selectedProjectId}
          onSelectProject={(id) => setSelectedProjectId(id || null)}
        />

        <div className="flex flex-col flex-1 min-w-0">

          {/* Top bar */}
          <header className="flex items-center gap-3 px-6 py-4 border-b border-border">
            <SidebarTrigger />
            <Separator orientation="vertical" className="h-5" />
            <span className="text-sm text-muted-foreground">
              {selectedProjectId
                ? projects.find(p => p.id === selectedProjectId)?.name
                : 'Dashboard'}
            </span>
          </header>

          {/* Main content */}
          <main className="flex-1 px-6 py-8">
            <div className="max-w-5xl mx-auto">

              <div className="mb-6">
                <h1 className="text-2xl font-semibold tracking-tight">
                  {selectedProjectId
                    ? projects.find(p => p.id === selectedProjectId)?.name
                    : 'Dashboard'}
                </h1>
                <p className="text-sm text-muted-foreground mt-1">
                  {selectedProjectId
                    ? projects.find(p => p.id === selectedProjectId)?.description
                    : 'Overview of all your monitored projects'}
                </p>
              </div>

              <Separator className="mb-6" />

              {/* Loading / Error */}
              {isLoading && <p className="text-sm text-muted-foreground">Loading...</p>}
              {isError && <p className="text-sm text-destructive">Failed to load projects.</p>}

              {/* Projects grid */}
              {!selectedProjectId && (
                <div className="flex flex-col gap-3">
                  {projects.map(project => (
                    <div key={project.id} onClick={() => setSelectedProjectId(project.id)}>
                      <ProjectCard project={project} />
                    </div>
                  ))}
                  {!isLoading && projects.length === 0 && (
                    <p className="text-sm text-muted-foreground">No projects found.</p>
                  )}
                </div>
              )}

              {/* Selected project logs */}
              {selectedProjectId && (
                <LogsTable projectId={selectedProjectId} />
              )}

            </div>
          </main>
        </div>
      </div>
    </SidebarProvider>
  )
}

export default App
