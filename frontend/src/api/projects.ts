import axios from 'axios'
import type { Project } from '../types/project'

const BASE_URL = 'http://localhost:8080'

export const fetchProjects = async (): Promise<Project[]> => {
  const { data } = await axios.get(`${BASE_URL}/api/projects`)
  return data
}
