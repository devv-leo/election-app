import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react'
import axios from 'axios'

interface User {
  username: string
  role: string
}

interface AuthContextType {
  user: User | null
  login: (username: string, password: string) => Promise<boolean>
  logout: () => void
  loading: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

interface AuthProviderProps {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const sessionId = localStorage.getItem('sessionId')
    if (sessionId) {
      axios.post('/api/validate-session', { sessionId })
        .then(response => {
          setUser(response.data)
        })
        .catch(() => {
          localStorage.removeItem('sessionId')
        })
        .finally(() => {
          setLoading(false)
        })
    } else {
      setLoading(false)
    }
  }, [])

  const login = async (username: string, password: string): Promise<boolean> => {
    try {
      const response = await axios.post('/api/login', { username, password })
      const { sessionId, username: user, role } = response.data
      
      localStorage.setItem('sessionId', sessionId)
      setUser({ username: user, role })
      return true
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  const logout = () => {
    const sessionId = localStorage.getItem('sessionId')
    if (sessionId) {
      axios.post('/api/logout', { sessionId })
        .finally(() => {
          localStorage.removeItem('sessionId')
          setUser(null)
        })
    }
  }

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  )
}
