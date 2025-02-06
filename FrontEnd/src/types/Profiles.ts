export interface Profile {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    projectDescription: string;
    sectorOfActivity: string;
    isActive: boolean | null;
    matchList: any | null;
}
