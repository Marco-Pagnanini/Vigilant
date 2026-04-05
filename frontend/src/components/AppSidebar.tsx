import { LayoutDashboard, FolderOpen, Settings, ChevronRight } from 'lucide-react'
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from '@/components/ui/sidebar'
import type { Project } from '../types/project'

interface AppSidebarProps {
  projects: Project[]
  selectedProjectId: string | null
  onSelectProject: (id: string) => void
}

export const AppSidebar = ({ projects, selectedProjectId, onSelectProject }: AppSidebarProps) => {
  return (
    <Sidebar>
      <SidebarHeader className="px-4 py-4 border-b border-border">
        <div className="flex items-center gap-2">
          <div className="w-6 h-6 rounded bg-primary flex items-center justify-center">
            <span className="text-primary-foreground text-xs font-bold">V</span>
          </div>
          <span className="font-semibold text-sm">Vigilant</span>
        </div>
      </SidebarHeader>

      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>General</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              <SidebarMenuItem>
                <SidebarMenuButton isActive={selectedProjectId === null} onClick={() => onSelectProject('')}>
                  <LayoutDashboard className="w-4 h-4" />
                  <span>Dashboard</span>
                </SidebarMenuButton>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <SidebarMenuButton>
                  <Settings className="w-4 h-4" />
                  <span>Settings</span>
                </SidebarMenuButton>
              </SidebarMenuItem>
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>

        <SidebarGroup>
          <SidebarGroupLabel>Projects</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {projects.map(project => (
                <SidebarMenuItem key={project.id}>
                  <SidebarMenuButton
                    isActive={selectedProjectId === project.id}
                    onClick={() => onSelectProject(project.id)}
                  >
                    <FolderOpen className="w-4 h-4" />
                    <span>{project.name}</span>
                    <ChevronRight className="w-3 h-3 ml-auto opacity-50" />
                  </SidebarMenuButton>
                </SidebarMenuItem>
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>

      <SidebarFooter className="border-t border-border px-4 py-3">
        <p className="text-xs text-muted-foreground">{projects.length} projects monitored</p>
      </SidebarFooter>
    </Sidebar>
  )
}
