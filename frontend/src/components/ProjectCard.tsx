import type { Project } from '../types/project'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'

interface ProjectCardProps {
  project: Project
}

export const ProjectCard = ({ project }: ProjectCardProps) => {
  return (
    <Card className="w-full cursor-pointer transition-all hover:ring-2 hover:ring-foreground/20">
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle className="text-base font-semibold">{project.name}</CardTitle>
        <Badge variant="outline" className="text-xs text-muted-foreground">
          {new Date(project.createdAt).toLocaleDateString()}
        </Badge>
      </CardHeader>
      <CardContent>
        <CardDescription>{project.description}</CardDescription>
      </CardContent>
    </Card>
  )
}
